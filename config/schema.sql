
    create table T_METRICAS (
        ID int4 not null,
        cores varchar(255),
        estagios varchar(255),
        intervalOuterRadius int4,
        label varchar(255),
        labelHeightAdjust int4,
        max float8,
        min float8,
        painel varchar(255),
        query varchar(255),
        showTickLabels boolean,
        style varchar(255),
        styleClass varchar(255),
        tipo varchar(255),
        titulo varchar(255),
        primary key (ID)
    );

    create sequence SEQ_METRICAS_ID;
