import argparse
from src.SorryServer import SorryServer

parser = argparse.ArgumentParser(description="Start the Sorry! server")
parser.add_argument("--host", default="127.0.0.1", type=str)
parser.add_argument("--port", default=12000, type=int)

args = parser.parse_args()

server = SorryServer(args)
server.run()
