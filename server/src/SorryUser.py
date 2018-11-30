import asyncio
import json
import re
from src.SorryGame import SorryGame

class SorryUser(asyncio.Protocol):

	def __init__(self, server):
		self.server = server
		self.transport = None
		self.username = None
		self.recvqueue = ""
		self.games = {}
		self.command_handlers = {
			"register_user": self.register_user,
			"get_game_list": self.get_game_list,
			"create_game": self.create_game,
			"join_game": self.join_game,
			"get_game_data": self.get_game_data,
			"update_pawn": self.update_pawn,
			"start_game": self.start_game,
			"player_won": self.player_won,
		}

	def write_message(self, json_message):
		message = json.dumps(json_message)
		print(f"Sent: {message}")
		self.transport.write(f"{message}\n".encode())

	def send_json(self, event_type, data):
		json_message = {"event": event_type, "data": data}
		self.write_message(json_message)

	def send_error(self, text, original_message):
		json_message = {"event": "error", "data": {"text": text, "original_message": original_message}}
		self.write_message(json_message)

	def connection_made(self, transport):
		self.transport = transport

	def connection_lost(self, exc):
		for game_name, game in self.games.items():
			game.remove_player(self, "connection closed")
		if self.username in self.server.current_users:
			del self.server.current_users[self.username]

	def data_received(self, data):
		self.parse_message(data.decode().strip("\r\n"))
		#messages = re.split("\n", data.decode())
		#[self.parse_message(msg) for msg in messages]

	def parse_message(self, msg):
		print(f"Received: {msg}")
		try:
			body = json.loads(msg)
		except:
			print(f"Couldn't decode JSON: {msg}")
			return self.send_error("Invalid JSON", msg)
		else:
			command = body.get("command")
			if not command:
				return self.send_error("No command specified", body)
			data = body.get("data")
			if data is None:
				return self.send_error("No data specified", body)

			command_handler = self.command_handlers.get(command)
			if not command_handler:
				self.send_error(f"Unknown command: {command}", body)

		command_handler(data)

	def register_user(self, data):
		username = data.get("username")
		if not username:
			return self.send_error("No username specified", data)
		if username in self.server.current_users:
			return self.send_error(f"{username} is already a user", data)
		self.username = username
		self.server.current_users[username] = self
		self.send_json("user_registered", {"username": username})

	def get_game_list(self, data):
		available_games = []
		for game_name, game_data in self.server.current_games.items():
			if game_data.state == "open":
				current_users = game_data.get_players_and_colors()
				available_games.append({
					game_name: {
						"players": current_users,
					},
				})

		self.send_json("game_list", {"games": available_games})

	def create_game(self, data):
		color = data.get("color")
		game_name = data.get("name")
		if not color:
			return self.send_error("No color specified", data)
		if not game_name:
			return self.send_error("No game name specified", data)
		if game_name in self.server.current_games:
			return self.send_error(f"Game {game_name} already exists", data)

		new_game = SorryGame(game_name, self.server)
		self.server.current_games[game_name] = new_game
		self.games[game_name] = new_game
		new_game.add_player(self, color)

	def join_game(self, data):
		color = data.get("color")
		game_name = data.get("name")
		if not game_name:
			return self.send_error("No game name specified", data)
		game = self.server.current_games.get(game_name)
		if not game:
			return self.send_error(f"The game {game_name} doesn't exist", data)
		current_users = game.get_players_and_colors()
		if self.username in current_users.keys():
			return self.send_error("User already in game", data)
		if not color:
			return self.send_error("No color specified", data)
		if color in current_users.values():
			return self.send_error(f"{color} already in use", data)

		game.add_player(self, color)

	def get_game_data(self, data):
		game_name = data.get("name")
		if not game_name:
			return self.send_error("No game name given", data)
		game = self.server.current_games.get(game_name)
		if not game:
			return self.send_error(f"No game named {game_name}", data)
		game.send_game_data(self)

	def update_pawn(self, data):
		game_name = data.get("game")
		if not game_name:
			return self.send_error("No game name given", data)
		game = self.server.current_games.get(game_name)
		if not game:
			return self.send_error(f"Game {game_name} doesn't exist", data)
		pawn = data.get("pawn")
		if not pawn:
			return self.send_error("No pawn given", data)
		new_position = data.get("new_position")
		if not new_position:
			return self.send_error("No new position given", data)
		turn_ends = data.get("turn_ends")
		if turn_ends is None:
			return self.send_error("Need to specify if the turn is ending", data)
		game.update_pawn(self, pawn, new_position, turn_ends)

	def start_game(self, data):
		game_name = data.get("game")
		if not game_name:
			return self.send_error("No game name given", data)
		game = self.server.current_games.get(game_name)
		if not game:
			return self.send_error(f"Game {game_name} doesn't exist", data)
		game.start_game()

	def player_won(self, data):
		game_name = data.get("game")
		if not game_name:
			return self.send_error("No game name given", data)
		game = self.server.current_games.get(game_name)
		if not game:
			return self.send_error(f"Game {game_name} doesn't exist", data)
		game.player_finished(self.username)
