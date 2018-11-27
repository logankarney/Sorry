class SorryGame(object):

	def __init__(self, name):
		self.name = name
		self.players = {}
		self.state = "open"
		self.board = {}

	def add_player(self, new_player, color):
		self.players[new_player] = {"color": color}
		self.board[new_player.username] = {}
		for pawn in [1, 2, 3, 4]:
			self.board[new_player.username][pawn] = f"{color[0].upper()}22"
		for player in self.players.keys():
			new_player_data = {"name": new_player.username, "color": color},
			player.send_json("player_joined", new_player_data)
			self.send_game_data(player)

	def remove_player(self, leaving_player, reason):
		leaving_player_data = {"player_name": leaving_player.username, "reason": reason}
		if leaving_player in self.players:
			del self.players[leaving_player]
			del self.board[leaving_player.username]

		for player in self.players.keys():
			player.send_json("player_left", leaving_player_data)
			self.send_game_data(player)

	def send_game_data(self, player):
		game = {"players": self.get_players_and_colors(), "board": self.board}
		player.send_json("game_data", game)

	def get_players_and_colors(self):
		return {k.username: v["color"] for k,v in self.players.items()}
