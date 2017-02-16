drop table Games;

drop xml schema collection GamesSchema;
create xml schema collection GamesSchema as
'<?xml version="1.0" encoding="utf-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="Data">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="baseImgUrl" type="xs:string" />
        <xs:element name="Game">
          <xs:complexType>
            <xs:sequence>
              <xs:element name="id" type="xs:unsignedByte" />
              <xs:element name="GameTitle" type="xs:string" />
              <xs:element name="PlatformId" type="xs:unsignedByte" />
              <xs:element name="Platform" type="xs:string" />
              <xs:element name="ReleaseDate" type="xs:string" />
              <xs:element name="Overview" type="xs:string" />
              <xs:element name="ESRB" type="xs:string" />
              <xs:element name="Genres">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="genre" type="xs:string" />
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element name="Players" type="xs:string" />
              <xs:element name="Co-op" type="xs:string" />
              <xs:element name="Youtube" type="xs:string" />
              <xs:element name="Publisher" type="xs:string" />
              <xs:element name="Developer" type="xs:string" />
              <xs:element name="Rating" type="xs:decimal" />
              <xs:element name="Similar">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="SimilarCount" type="xs:unsignedByte" />
                    <xs:element maxOccurs="unbounded" name="Game">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="id" type="xs:unsignedShort" />
                          <xs:element name="PlatformId" type="xs:unsignedByte" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element name="Images">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element maxOccurs="unbounded" name="fanart">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="original">
                            <xs:complexType>
                              <xs:simpleContent>
                                <xs:extension base="xs:string">
                                  <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                                  <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                                </xs:extension>
                              </xs:simpleContent>
                            </xs:complexType>
                          </xs:element>
                          <xs:element name="thumb" type="xs:string" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                    <xs:element maxOccurs="unbounded" name="boxart">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="side" type="xs:string" use="required" />
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="thumb" type="xs:string" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                    <xs:element maxOccurs="unbounded" name="banner">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedByte" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                    <xs:element name="screenshot">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="original">
                            <xs:complexType>
                              <xs:simpleContent>
                                <xs:extension base="xs:string">
                                  <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                                  <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                                </xs:extension>
                              </xs:simpleContent>
                            </xs:complexType>
                          </xs:element>
                          <xs:element name="thumb" type="xs:string" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                    <xs:element name="clearlogo">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedByte" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
            </xs:sequence>
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
</xs:schema>';
go

drop xml schema collection ArtSchema;
create xml schema collection ArtSchema as
'<?xml version="1.0" encoding="utf-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="Data">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="baseImgUrl" type="xs:string" />
        <xs:element name="Images">
          <xs:complexType>
            <xs:sequence>
              <xs:element maxOccurs="unbounded" name="fanart">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="original">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                    <xs:element name="thumb" type="xs:string" />
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element maxOccurs="unbounded" name="boxart">
                <xs:complexType>
                  <xs:simpleContent>
                    <xs:extension base="xs:string">
                      <xs:attribute name="side" type="xs:string" use="required" />
                      <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                      <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                      <xs:attribute name="thumb" type="xs:string" use="required" />
                    </xs:extension>
                  </xs:simpleContent>
                </xs:complexType>
              </xs:element>
              <xs:element maxOccurs="unbounded" name="banner">
                <xs:complexType>
                  <xs:simpleContent>
                    <xs:extension base="xs:string">
                      <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                      <xs:attribute name="height" type="xs:unsignedByte" use="required" />
                    </xs:extension>
                  </xs:simpleContent>
                </xs:complexType>
              </xs:element>
              <xs:element name="screenshot">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="original">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                    <xs:element name="thumb" type="xs:string" />
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element name="clearlogo">
                <xs:complexType>
                  <xs:simpleContent>
                    <xs:extension base="xs:string">
                      <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                      <xs:attribute name="height" type="xs:unsignedByte" use="required" />
                    </xs:extension>
                  </xs:simpleContent>
                </xs:complexType>
              </xs:element>
            </xs:sequence>
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
</xs:schema>';
go

create table Games(
	Id int primary key,
	Info xml (document GamesSchema) null,
	Art xml (document ArtSchema) null,
);
go

drop table Scores;

drop xml schema collection ScoresSchema;
create xml schema collection ScoresSchema as
'<?xml version="1.0" encoding="utf-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="Ratings">
    <xs:complexType>
      <xs:sequence>
        <xs:element maxOccurs="unbounded" name="User">
          <xs:complexType>
            <xs:sequence>
              <xs:element maxOccurs="unbounded" name="Rating">
                <xs:complexType>
                  <xs:attribute name="idJogo" type="xs:unsignedShort" use="required" />
                  <xs:attribute name="Rating" type="xs:unsignedByte" use="required" />
                </xs:complexType>
              </xs:element>
            </xs:sequence>
            <xs:attribute name="id" type="xs:string" use="required" />
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
</xs:schema>';
go

create table Scores(
	Id int identity(1, 1) primary key,
	Scores xml (document ScoresSchema)
);

insert into Scores values ('<Ratings></Ratings>');
go