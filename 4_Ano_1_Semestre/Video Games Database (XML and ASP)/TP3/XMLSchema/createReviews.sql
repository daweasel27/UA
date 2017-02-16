drop table Reviews;

drop xml schema collection ReviewSchema;
create xml schema collection ReviewSchema as
'<?xml version="1.0" encoding="utf-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="Reviews">
    <xs:complexType>
      <xs:sequence>
        <xs:element maxOccurs="unbounded" minOccurs="0" name="User">
          <xs:complexType>
            <xs:attribute name="id" type="xs:string" use="required" />
            <xs:attribute name="idJogo" type="xs:unsignedInt" use="required" />
            <xs:attribute name="review" type="xs:string" use="required" />
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
</xs:schema>';
go

create table Reviews(
	Id int identity(1, 1) primary key,
	Reviews xml (document ReviewSchema)
);

insert into Reviews values ('<Reviews></Reviews>');

go
select * from Reviews