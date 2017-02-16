alter proc InsertPlayed(@UserId nvarchar(128), @GameId int)
as begin
	if not exists(select * from Played where Played.exist('/Played/User[@id=sql:variable("@UserId")]') = 1)
		update Played set Played.modify(
			'insert <User id="{sql:variable("@UserId")}"></User>
			 into (/Played)[1]');
	if not exists(select * from Played where Played.exist('/Played/User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")]') = 1)
		update Played set Played.modify(
			'insert <Game idJogo="{sql:variable("@GameId")}" played="true"></Game>
				into (/Played/User[@id=sql:variable("@UserId")])[1]');
end
exec InsertPlayed @UserId = 'Joao', @GameId = 123
go

alter proc ScoreGame(@UserId nvarchar(128), @GameId int, @Score int)
as begin
	if not exists(select * from Played where Played.exist('/Played/User[@id=sql:variable("@UserId")]') = 1)
	begin
		update Played set Played.modify(
			'insert <User id="{sql:variable("@UserId")}"></User>
			 into (/Played)[1]');
		update Played set Played.modify(
			 'insert <Game idJogo="{sql:variable("@GameId")}" played="true" score="{sql:variable("@Score")}"></Game>
			 into (/Played/User[@id=sql:variable("@UserId")])[1]');
	end
	else 
		
		if not exists(select * from Played where Played.exist('/Played/User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")]') = 1)
		BEGIN
			update Played set Played.modify(
			 'insert <Game idJogo="{sql:variable("@GameId")}" played="true" score="{sql:variable("@Score")}"></Game>
			 into (/Played/User[@id=sql:variable("@UserId")])[1]');
		END
		else 
		BEGIN
			update Played set Played.modify(
				'delete (/Played/User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")])[1]');
			update Played set Played.modify(
				'insert <Game idJogo="{sql:variable("@GameId")}" played="true" score="{sql:variable("@Score")}"></Game>
				 into (/Played/User[@id=sql:variable("@UserId")])[1]');
				 END

end
exec InsertPlayed @UserId = 'Vitor', @GameId = 223
exec ScoreGame @UserId = 'Andre', @GameId = 323, @Score = 4
go

create proc DeletePlayed(@UserId nvarchar(128), @GameId int)
as begin
	update Played set Played.modify(
		'delete (/Played/User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")])[1]');
end
exec DeletePlayed @UserId = 'Joao', @GameId =223
go


ALTER function ReturnGames()
returns table
as
 return(
	select TOP 10 Info.value('(//Game/id)[1]','int') as id FROM Games 
);
	select TOP 10 Info.value('(//Game/id)[1]','int') as id FROM Games 

select * from  ReturnGames()

alter proc getscoreProc(@UserId nvarchar(128), @GameId int)
as
	declare @total int
 	select  @total = Played.value('(//User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")]/@score)[1]','int') FROM Played			
	return @total

DECLARE @SomeValue int
exec @SomeValue = getscoreProc @UserId = "joao@ua.pt", @GameId =21182
SELECT @SomeValue

alter proc returnUserAndGamesAndScores1 @GameId int, @Data XML  OUTPUT 
as
  BEGIN 
 	DECLARE @cust XML
	SET @cust = (select Played.query('(//User/Game[@idJogo=sql:variable("@GameId")])') as id FROM Played	)	
	set @Data  =@cust
  END 

DECLARE  @a XML 
EXEC returnUserAndGamesAndScores1 @GameId=2333 ,@Data =@a OUT 
SELECT @a 

alter proc Playing(@UserId nvarchar(128), @GameId int)
as
	if exists(select * from Played where Played.exist('/Played/User[@id=sql:variable("@UserId")]/Game[@idJogo=sql:variable("@GameId")]') = 1)
		return 1;
	return 0;

DECLARE @SomeValue int
exec @SomeValue = Playing @UserId = "admin@admin.pt", @GameId = 211822
select @SomeValue

create function returngamesplayed5(@GameId nvarchar(30))
returns NVARCHAR(4000)
as
begin
	DECLARE @FullInfo xml
	declare @string NVARCHAR(4000)

 set  @FullInfo = ( select Played.query('(//User[@id=sql:variable("@GameId")]/Game)') as id from Played)
 	SET @string = CONVERT(VARCHAR(4000), @FullInfo);

 return @string
 end

alter function returngamesscore3(@GameId int)
returns NVARCHAR(4000)
as
begin
declare @string NVARCHAR(4000)
	DECLARE @FullInfo xml
	set @FullInfo = (select Played.query('(//User/Game[@idJogo=sql:variable("@GameId")])') as id from Played)
	SET @string = CONVERT(VARCHAR(4000), @FullInfo);
 return @string
 end

select dbo.returngamesplayed5('admin@admin.pt')
select * from returngamesscore3()

SELECT * FROM Games

create proc CheckAvg(@GameId int)
as begin
	select Played.value('(//Game[@idJogo=sql:variable("@GameId")]/@score)[1]' ,'int') FROM Played
end

alter function CheckValues()
returns table
as
	return(
	
		select 
			Played.value('(//Game/@idJogo)[1]','int') as id  From Played  
	
);

	select * from CheckValues()
	select * from Played
	select * from Reviews