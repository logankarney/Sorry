import asyncio
from src.SorryUser import SorryUser

class SorryServer(object):

	def __init__(self, args):
		self.current_games = {}
		self.current_users = {}
		self.async_loop = asyncio.get_event_loop()
		self.async_server = self.async_loop.create_server(
			lambda: SorryUser(self), args.host, args.port)

	def run(self):
		self.async_loop.run_until_complete(self.async_server)
		self.async_loop.run_forever()
