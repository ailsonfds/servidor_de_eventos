import psycopg2
from psycopg2.extras import DictCursor
import json

def get_connection():
    con = psycopg2.connect(host='ec2-184-73-169-163.compute-1.amazonaws.com', database='d5q90e53194jfv', port='5432', user='eegzpuodqdpkcp', password='f30e52b6c4a89337c32241cd2b28485d2f8d2a523de7efe72c8b51be055d5891')
    return con

'''
                                      Tabela "public.event"
   Coluna    |       Tipo        | Collation | Nullable |                 Default                 
-------------+-------------------+-----------+----------+-----------------------------------------
 id_event    | integer           |           | not null | nextval('event_id_event_seq'::regclass)
 name        | character varying |           | not null | 
 description | character varying |           | not null | 
 author      | character varying |           | not null | 
 created     | date              |           | not null | 
 end_date    | date              |           | not null | 
Índices:
    "event_pkey" PRIMARY KEY, btree (id_event)
Restrições de chave estrangeira:
    "event_author_fkey" FOREIGN KEY (author) REFERENCES person(username) ON DELETE CASCADE
Referenciada por:
    TABLE "event_topic" CONSTRAINT "event_topic_id_event_fkey" FOREIGN KEY (id_event) REFERENCES event(id_event) ON DELETE CASCADE


                    Tabela "public.topic"
 Coluna |       Tipo        | Collation | Nullable | Default 
--------+-------------------+-----------+----------+---------
 name   | character varying |           | not null | 
Índices:
    "topic_pkey" PRIMARY KEY, btree (name)
Referenciada por:
    TABLE "event_topic" CONSTRAINT "event_topic_topic_fkey" FOREIGN KEY (topic) REFERENCES topic(name) ON DELETE CASCADE
    TABLE "person_topic" CONSTRAINT "person_topic_topic_fkey" FOREIGN KEY (topic) REFERENCES topic(name) ON DELETE CASCADE


                  Tabela "public.event_topic"
  Coluna  |       Tipo        | Collation | Nullable | Default 
----------+-------------------+-----------+----------+---------
 topic    | character varying |           | not null | 
 id_event | integer           |           | not null | 
Restrições de chave estrangeira:
    "event_topic_id_event_fkey" FOREIGN KEY (id_event) REFERENCES event(id_event) ON DELETE CASCADE
    "event_topic_topic_fkey" FOREIGN KEY (topic) REFERENCES topic(name) ON DELETE CASCADE

'''
def insert_event(data: dict):

    con = get_connection()
    cur = con.cursor()
    sql = "INSERT INTO public.event (name,description,author,created,end_date) VALUES (%s,%s,%s,%s,%s) RETURNING id_event"
    sql_data = (data['name'],data['description'],data['author'],data['created'],data['end_date'])
    cur.execute(sql,sql_data)
    id_event = cur.fetchone()
    print(id_event)
    for topic in data['topics']:
        sql = "SELECT * FROM event_topic WHERE topic = %s AND id_event = %s"
        sql_data = (topic,id_event)
        cur.execute(sql,sql_data)
        event_topic = cur.fetchone()

        if event_topic is None:
            sql = "SELECT * FROM topic WHERE topic.name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            topic_exists = cur.fetchone()

            if topic_exists is None:
                sql = "INSERT INTO topic (name) VALUES (%s)"
                sql_data = (topic,)
                cur.execute(sql,sql_data)

            sql = "INSERT INTO event_topic (topic,id_event) VALUES (%s,%s)"
            sql_data = (topic,id_event)
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def insert_topics(data: dict):
    con = get_connection()
    cur = con.cursor()
    
    for topic in data['topics']:
        sql = "SELECT * FROM event_topic WHERE topic = %s AND id_event = %s"
        sql_data = (topic,data['id_event'])
        cur.execute(sql,sql_data)
        event_topic = cur.fetchone()

        if event_topic is None:
            sql = "SELECT * FROM topic WHERE name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            topic_exists = cur.fetchone()

            if topic_exists is None:
                sql = "INSERT INTO topic (name) VALUES (%s)"
                sql_data = (topic,)
                cur.execute(sql,sql_data)

            sql = "INSERT INTO event_topic (topic,id_event) VALUES (%s,%s)"
            sql_data = (topic,data['id_event'])
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def delete_topics(data: dict):
    con = get_connection()
    cur = con.cursor()
    
    for topic in data['topics']:
        sql = "SELECT * FROM event_topic WHERE topic = %s AND id_event = %s"
        sql_data = (topic,data['id_event'])
        cur.execute(sql,sql_data)
        event_topic = cur.fetchone()

        if event_topic is not None:
            sql = "SELECT * FROM event_topic WHERE topic = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            events_with_topic = cur.fetchall()

            sql = "SELECT * FROM person_topic WHERE topic = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            persons_with_topic = cur.fetchall()

            if len(events_with_topic) == 1 and len(persons_with_topic) == 0:
                sql = "DELETE FROM topic WHERE name = %s"
                sql_data = (topic,)
                cur.execute(sql,sql_data)

            sql = "DELETE FROM event_topic WHERE topic = %s AND id_event = %s"
            sql_data = (topic,data['id_event'])
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def select_events():
    con = get_connection()
    cur = con.cursor(cursor_factory=DictCursor)
    
    sql = "SELECT * FROM event"
    cur.execute(sql)
    events = [(dict(record)) for record in cur]

    for event in events:
        cur.execute("SELECT topic FROM event_topic WHERE id_event = " + str(event['id_event']))
        topics = []
        [topics.extend(topic) for topic in cur]
        event['topics'] = topics
    
    return events


def select_events_by_topic(topic):
    con = get_connection()
    cur = con.cursor(cursor_factory=DictCursor)
    
    sql = '''SELECT event.id_event, event.name, event.description, event.author, event.created, event.end_date 
                FROM (event JOIN event_topic on event.id_event = event_topic.id_event)
                WHERE event_topic.topic = %s  
          '''
    cur.execute(sql, (topic,))
    events = [(dict(record)) for record in cur]

    for event in events:
        cur.execute("SELECT topic FROM event_topic WHERE id_event = " + str(event['id_event']))
        topics = []
        [topics.extend(topic) for topic in cur]
        event['topics'] = topics
    
    return events


def event_exists_by_id(id_event):
    con = get_connection()
    cur = con.cursor()
    
    cur.execute("SELECT * FROM event WHERE id_event = %s", (id_event,))

    event = cur.fetchone()
    cur.close()
    con.close()

    if event is not None:
        return True
    return False

'''
                    Tabela "public.person"
  Coluna  |       Tipo        | Collation | Nullable | Default 
----------+-------------------+-----------+----------+---------
 username | character varying |           | not null | 
 name     | character varying |           | not null | 
 password | character varying |           | not null | 
 logged   | boolean           |           | not null | 
Índices:
    "person_pkey" PRIMARY KEY, btree (username)
Referenciada por:
    TABLE "event" CONSTRAINT "event_author_fkey" FOREIGN KEY (author) REFERENCES person(username) ON DELETE CASCADE
    TABLE "person_topic" CONSTRAINT "person_topic_username_fkey" FOREIGN KEY (username) REFERENCES person(username) ON DELETE CASCADE
'''
def insert_interest_topics(data: dict):
    con = get_connection()
    cur = con.cursor()

    for topic in data['topics']:
        sql = "SELECT * FROM person_topic WHERE topic = %s AND username = %s"
        sql_data = (topic,data['username'])
        cur.execute(sql,sql_data)
        person_topic = cur.fetchone()

        if person_topic is None:
            sql = "SELECT * FROM topic WHERE name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            topic_exists = cur.fetchone()

            if topic_exists is None:
                sql = "INSERT INTO topic (name) VALUES (%s)"
                sql_data = (topic,)
                cur.execute(sql,sql_data)

            sql = "INSERT INTO person_topic (topic,username) VALUES (%s,%s)"
            sql_data = (topic,data['username'])
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def remove_interest_topics(data: dict):
    con = get_connection()
    cur = con.cursor()

    for topic in data['topics']:
        sql = "SELECT * FROM person_topic WHERE topic = %s AND username = %s"
        sql_data = (topic,data['username'])
        cur.execute(sql,sql_data)
        person_topic = cur.fetchone()

        if person_topic is not None:
            sql = "SELECT * FROM person_topic WHERE topic = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            persons_with_topic = cur.fetchall()

            sql = "SELECT * FROM event_topic WHERE topic = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            events_with_topic = cur.fetchall()

            if len(persons_with_topic) == 1 and len(events_with_topic) == 0:
                sql = "DELETE FROM topic WHERE name = %s"
                sql_data = (topic,)
                cur.execute(sql,sql_data)

            sql = "DELETE FROM person_topic WHERE topic = %s AND username = %s"
            sql_data = (topic,data['username'])
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def insert_user(data: dict):
    con = get_connection()
    cur = con.cursor()

    sql = "INSERT INTO person (username,name,password,logged) VALUES (%s,%s,%s,%s)"
    sql_data = (data['username'],data['name'],data['password'],False)
    cur.execute(sql,sql_data)
    con.commit()

    cur.close()
    con.close()

def user_already_logged(username):
    con = get_connection()
    cur = con.cursor()

    cur.execute("SELECT * FROM person WHERE username = %s", (username,))
    
    user_logged = cur.fetchone()[3]
    cur.close()
    con.close()

    if user_logged == True:
        return True
    return False

    

def username_already_exists(username):
    con = get_connection()
    cur = con.cursor()
    
    cur.execute("SELECT * FROM person WHERE username = %s", (username,))

    user = cur.fetchone()
    cur.close()
    con.close()

    if user is not None:
        return True
    return False


def credentials_exists(data: dict):
    con = get_connection()
    cur = con.cursor()
    
    cur.execute("SELECT * FROM person WHERE username = %s AND password = %s", (data['username'],data['password']))

    user = cur.fetchone()
    cur.close()
    con.close()

    if user is not None:
        return False
    return True


def get_user_info(username):
    con = get_connection()
    cur = con.cursor()
    
    cur.execute("SELECT * FROM person WHERE username = %s", (username,))
    
    user_tuple = cur.fetchone()
    user = {"username": user_tuple[0],
            "name": user_tuple[1],
            "password": user_tuple[2],
            "logged": user_tuple[3]
        }

    cur.close()
    con.close()

    return user


def login_user(username):
    con = get_connection()
    cur = con.cursor()

    cur.execute("UPDATE person SET logged = %s WHERE username = %s",(True, username))

    con.commit()
    cur.close()
    con.close()


def logout_user(username):
    con = get_connection()
    cur = con.cursor()

    cur.execute("UPDATE person SET logged = %s WHERE username = %s",(False, username))

    con.commit()
    cur.close()
    con.close()
