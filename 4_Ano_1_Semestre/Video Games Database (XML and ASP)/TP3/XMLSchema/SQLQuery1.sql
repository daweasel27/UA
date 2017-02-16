ALTER function GamesInfo(@Id int)
returns @FullInfo table(
	FullInfo xml not null
)
as begin
	declare @Info xml;

	insert into @FullInfo values ('<Data> </Data>');

	select  @Info = Info.query('/Data/Game')
	from Games
	where Info.exist('/Data/Game[id=sql:variable("@Id")]') = 1;

	update @FullInfo set FullInfo.modify('insert sql:variable("@Info") into (/Data)[1]');
	
	return;
end