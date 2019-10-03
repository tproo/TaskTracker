import sys
import database_struct
import psycopg2

if __name__=="__main__":
    database_struct.clearDatabase()
    database_struct.Base.metadata.create_all(database_struct.engine)

