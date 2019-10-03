import os
import sys
from sqlalchemy import Column, ForeignKey, Integer, String, Sequence
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine
from psycopg2 import connect


user_ = 'postgres'
password_ = '!QAZ2wsx'
host_ = 'localhost'
database_ = 'task_tracker'


def clearDatabase():
    con = connect(user=user_,
            password=password_,
            host=host_,
            database='postgres')
    with con.cursor() as cursor:
        con.autocommit = True
        cursor.execute("drop database if exists {};".format(database_))
        cursor.execute("create database {} owner={} encoding='UTF8';".format(database_, user_))


Base = declarative_base()


#class Person(Base):
#    __tablename__ = 'persons'
#    id = Column(Integer, autoincrement=1, primary_key=True)
#    name = Column(String(250), nullable=False)


class Task(Base):
    __tablename__ = 'tasks'
    id = Column(Integer, autoincrement=True, primary_key=True)
    title = Column(String(250), nullable=False)
    description = Column(String(250), nullable=True)
    parent = Column(Integer, ForeignKey('tasks.id'))
    #owner = Column(String(250), ForeignKey('persons.id'))


engine = create_engine('postgresql+psycopg2://postgres:!QAZ2wsx@localhost/task_tracker')

