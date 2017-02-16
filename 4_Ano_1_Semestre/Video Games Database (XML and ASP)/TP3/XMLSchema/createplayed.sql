drop table Played;

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

drop xml schema collection PlayedSchema;
create xml schema collection PlayedSchema as
'<?xml version="1.0" encoding="utf-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="Played">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" maxOccurs="unbounded" name="User">
          <xs:complexType>
            <xs:sequence>
              <xs:element minOccurs="0" maxOccurs="unbounded" name="Game">
                <xs:complexType>
                  <xs:attribute name="idJogo" type="xs:unsignedInt" use="required" />
                  <xs:attribute name="played" type="xs:boolean" use="required" />
                  <xs:attribute name="score" use="optional" type="scoreType" >
                  </xs:attribute>
                </xs:complexType>
              </xs:element>
            </xs:sequence>
            <xs:attribute name="id" type="xs:string" use="required" />
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
  <xs:simpleType name="scoreType">
    <xs:restriction base="xs:integer">
    <xs:minInclusive value="1" />
    <xs:maxInclusive value="5" />
    </xs:restriction>
</xs:simpleType>
</xs:schema>';
go

create table Played(
	Id int identity(1, 1) primary key,
	Played xml (document PlayedSchema)
);

insert into Played values ('<Played></Played>');

go
select * from played