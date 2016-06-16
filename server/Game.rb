class Game
	attr_accessor :currentPlayer
	def initialize
		@board = Array.new(9, nil)
	end
	
	def hasWinner
		return (@board[0] != nil && @board[0] == @board[1] && @board[0] == @board[2])||(@board[3] != nil && @board[3] == @board[4] && @board[3] == @board[5])||(@board[6] != nil && @board[6] == @board[7] && @board[6] == @board[8])||(@board[0] != nil && @board[0] == @board[3] && @board[0] == @board[6])||(@board[1] != nil && @board[1] == @board[4] && @board[1] == @board[7])||(@board[2] != nil && @board[2] == @board[5] && @board[2] == @board[8])||(@board[0] != nil && @board[0] == @board[4] && @board[0] == @board[8])||(@board[2] != nil && @board[2] == @board[4] && @board[2] == @board[6])
	end

	def boardFilledUp
		for i in 0..@board.length-1
			return false if (@board[i] == nil)
		end
	end

	def legalMove(loc, player)
		if (player==@currentPlayer && @board[loc] == nil)
			puts "Ход корректен."
			@board[loc] = @currentPlayer;
			@currentPlayer = @currentPlayer.opponent
			@currentPlayer.otherPlayerMoved(loc)
			return true
		else 
			puts "Ход не корректен. Верный игрок? - #{player==@currentPlayer} Ячейка свободна? - #{(@board[loc] == nil)}"
			return false
		end
	end
	
end