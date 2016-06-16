class Player
	attr_accessor :opponent
	def initialize(connection, mark, game)
		@game = game
		@connection = connection
		@mark = mark
		connection.puts "WELCOME #{@mark}"
		connection.puts "MESSAGE op. waiting"
	end
	


	def otherPlayerMoved(loc)
		@connection.puts "OPPONENT_MOVED #{loc}"
		@connection.puts "DEFEAT" if @game.hasWinner()
		@connection.puts "TIE" if @game.boardFilledUp()	
	end

	def run
		Thread.new {
			@connection.puts "MESSAGE READY!"

			if (@mark == :X)
				@connection.puts "MESSAGE your turn"
			end

			loop do
				Thread.new {
					command = @connection.gets
					if (command.start_with?("MOVE"))
						loc = command[5..-1]
						puts loc
						if (@game.legalMove(loc.to_i, self))
							puts "ход корректен"
							@connection.puts "VALID_MOVE"
							@connection.puts "VICTORY" if @game.hasWinner()
							@connection.puts "TIE" if @game.boardFilledUp()
						else
							@connection.puts "MESSAGE wrong move"
						end
					end
					break if( command.start_with?("QUIT"))
				}
			end
			puts "игра завершена"
		# ensure	@connection.close;
		}
	end
end