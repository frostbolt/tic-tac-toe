require "socket"
require_relative "Player"
require_relative "Game"

class TTTServer
	def initialize()
		@server = TCPServer.open("192.168.0.16", 65500);
		puts "#{Time.now.hour}:#{Time.now.min}:#{Time.now.sec} Игровой сервер запущен."
		main
	end	

	def main
		loop do	
			game = Game.new
			client1 = @server.accept
			playerX = Player.new(client1, :X, game)
			client2 =  @server.accept
			playerY = Player.new(client2, :O, game)
			playerX.opponent = playerY
			playerY.opponent = playerX
			game.currentPlayer = playerX
			playerX.run
			playerY.run
			puts "Игра началась #{client1}  x  #{client2}"
		end

	ensure @server.close  # finally
		
	end

end

TTTServer.new