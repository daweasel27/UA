﻿drop table Games;

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
              <xs:element name="id" type="xs:unsignedInt" />
              <xs:element name="GameTitle" type="xs:string" />
              <xs:element name="AlternateTitles" minOccurs="0" >
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="title" type="xs:string" minOccurs="0"  />
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element name="PlatformId" type="xs:unsignedInt" minOccurs="0" />
              <xs:element name="Platform" type="xs:string" minOccurs="0" />
              <xs:element name="ReleaseDate" type="xs:string" minOccurs="0" />
              <xs:element name="Overview" type="xs:string" minOccurs="0" />
              <xs:element name="ESRB" type="xs:string" minOccurs="0" />
              <xs:element name="Genres" minOccurs="0">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="genre" type="xs:string" minOccurs="0" maxOccurs="unbounded" />
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element name="Players" minOccurs="0" type="xs:string" />
              <xs:element name="Co-op" minOccurs="0" type="xs:string" />
              <xs:element name="Youtube" minOccurs="0" type="xs:string" />
              <xs:element name="Publisher" minOccurs="0" type="xs:string" />
              <xs:element name="Developer" minOccurs="0" type="xs:string" />
              <xs:element name="Rating" minOccurs="0" type="xs:decimal" />
              <xs:element name="Similar" minOccurs="0">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="SimilarCount" minOccurs="0" type="xs:unsignedByte" />
                    <xs:element maxOccurs="unbounded" minOccurs="0" name="Game">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element name="id" minOccurs="0" type="xs:unsignedInt" />
                          <xs:element name="PlatformId" minOccurs="0" type="xs:unsignedInt" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                  </xs:sequence>
                </xs:complexType>
              </xs:element>
              <xs:element minOccurs="0" name="Images">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element maxOccurs="unbounded" minOccurs="0" name="fanart">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element minOccurs="0" name="original">
                            <xs:complexType>
                              <xs:simpleContent>
                                <xs:extension base="xs:string">
                                  <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                                  <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                                </xs:extension>
                              </xs:simpleContent>
                            </xs:complexType>
                          </xs:element>
                          <xs:element minOccurs="0" name="thumb" type="xs:string" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                    <xs:element minOccurs="0" maxOccurs="unbounded" name="boxart">
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
                    <xs:element minOccurs="0" maxOccurs="unbounded" name="banner">
                      <xs:complexType>
                        <xs:simpleContent>
                          <xs:extension base="xs:string">
                            <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                            <xs:attribute name="height" type="xs:unsignedByte" use="required" />
                          </xs:extension>
                        </xs:simpleContent>
                      </xs:complexType>
                    </xs:element>
                    <xs:element minOccurs="0" maxOccurs="unbounded" name="screenshot">
                      <xs:complexType>
                        <xs:sequence>
                          <xs:element minOccurs="0" maxOccurs="unbounded" name="original">
                            <xs:complexType>
                              <xs:simpleContent>
                                <xs:extension base="xs:string">
                                  <xs:attribute name="width" type="xs:unsignedShort" use="required" />
                                  <xs:attribute name="height" type="xs:unsignedShort" use="required" />
                                </xs:extension>
                              </xs:simpleContent>
                            </xs:complexType>
                          </xs:element>
                          <xs:element minOccurs="0" name="thumb" type="xs:string" />
                        </xs:sequence>
                      </xs:complexType>
                    </xs:element>
                    <xs:element minOccurs="0" maxOccurs="unbounded" name="clearlogo">
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



create table Games(
	Id int primary key,
	Info xml (document GamesSchema) null
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
                  <xs:attribute name="idJogo" type="xs:unsignedInt" use="required" />
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

go
