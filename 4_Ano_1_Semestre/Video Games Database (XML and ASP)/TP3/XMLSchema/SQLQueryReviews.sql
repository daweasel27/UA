alter proc InsertReview(@UserId nvarchar(128), @Review nvarchar(4000), @GameId int)
as begin
		update Reviews set Reviews.modify(
			'insert <User id="{sql:variable("@UserId")}" idJogo="{sql:variable("@GameId")}" review="{sql:variable("@Review")}"></User>
				into (/Reviews)[1]');
end
exec InsertReview @UserId = 'Joao', @Review = 'O jogo é muito fixe', @GameId=232
go

alter proc DeleteReview(@UserId nvarchar(128), @Review nvarchar(4000), @GameId int)
as begin
	update Reviews set Reviews.modify(
		'delete (/Reviews/User[@id=sql:variable("@UserId") and @review=sql:variable("@Review") and @idJogo=sql:variable("@GameId")])[1]');
end
exec DeleteReview @UserId = 'Joao', @Review = 'O jogo é muito fixe', @GameId=232
go

create function returnReviewsUser(@userId nvarchar(30))
returns NVARCHAR(4000)
as
begin
	DECLARE @FullInfo xml
	declare @string NVARCHAR(4000)

 set  @FullInfo = ( select Reviews.query('(//User[@id=sql:variable("@userId")])') as id from Reviews)
 	SET @string = CONVERT(VARCHAR(4000), @FullInfo);

 return @string
 end

 create function returnReviewsGame(@GameId int)
returns NVARCHAR(4000)
as
begin
	DECLARE @FullInfo xml
	declare @string NVARCHAR(4000)

 set  @FullInfo = ( select Reviews.query('(//User[@idJogo=sql:variable("@GameId")])') as id from Reviews)
 	SET @string = CONVERT(VARCHAR(4000), @FullInfo);

 return @string
 end

select dbo.returnReviewsGame(7402
)

select * from Reviews