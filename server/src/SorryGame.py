class SorryGame(object):

	def __init__(self, name):
		self.name = name
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
			new_player_data = {"name": new_player.username, "color": color},
			player.send_json("player_joined", new_player_data)
			self.send_game_data(player)

	def remove_player(self, leaving_player, reason):
		leaving_player_data = {"player_name": leaving_player.username, "reason": reason}
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
		game = {"players": self.get_players_and_colors(), "turns": self.turns, "board": self.board}
		player.send_json("game_data", game)

	def get_players_and_colors(self):
		return {k.username: v["color"] for k,v in self.players.items()}
