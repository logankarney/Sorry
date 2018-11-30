class SorryGame(object):

	def __init__(self, name, server):
		self.name = name
		self.server = server
		self.players = {}
		self.turns = []
		self.state = "open"
		self.board = {}

	def add_player(self, new_player, color):
		self.players[new_player] = {"color": color}
		color_initial = color[0].upper()
		self.turns.append(new_player.username)
		for pawn_number in [1, 2, 3, 4]:
			pawn = f"{color_initial}{pawn_number}"
			self.board[pawn] = f"{color_initial}22"
		for player in self.players.keys():
			new_player_data = {"game": self.name, "name": new_player.username, "color": color},
			player.send_json("player_joined", new_player_data)
			self.send_game_data(player)

		if len(self.players) == 4:
			self.start_game()

	def remove_player(self, leaving_player, reason):
		leaving_player_data = {"game": self.name, "player_name": leaving_player.username, "reason": reason}
		leaving_player_color = self.players[leaving_player]["color"][0].upper()
		self.board = {k:v for k,v in self.board.items() if not k.startswith(leaving_player_color)}
		self.turns.remove(leaving_player.username)
		del self.players[leaving_player]

		if len(self.players) == 0:
			del self.server.current_games[self.name]
			return

		for player in self.players.keys():
			player.send_json("player_left", leaving_player_data)
			self.send_game_data(player)

	def send_game_data(self, player):
		game = {"game": self.name, "players": self.get_players_and_colors(), "turns": self.turns, "board": self.board}
		player.send_json("game_data", game)

	def get_players_and_colors(self):
		return {k.username: v["color"] for k,v in self.players.items()}

	def update_pawn(self, player, pawn, new_position, turn_ends):
		self.board[pawn] = new_position
		new_pawn_data = {
			"game": self.name,
			"player": player.username,
			"pawn": pawn,
			"new_position": new_position,
		}
		for player in self.players.keys():
			player.send_json("pawn_updated", new_pawn_data)

		if turn_ends:
			self.turns.append(self.turns.pop(0))
			for player in self.players.keys():
				player.send_json("next_turn", {"game": self.name, "player": self.turns[0]})

	def start_game(self):
		self.state = "started"
		for player in self.players.keys():
			player.send_json("game_started", {"game": self.name})
			player.send_json("next_turn", {"game": self.name, "player": self.turns[0]})

	def player_finished(self, player):
		self.turns.remove(player.username)
		for player in self.players.keys():
			player.send_json("player_finished", {"game": self.name, "player": player.username})
