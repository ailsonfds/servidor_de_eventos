import psycopg2

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
    sql = "INSERT INTO event (name,description,author,created,end_date) VALUES (%s,%s,%s,%s,%s)"
    sql_data = (data['name'],data['description'],data['author'],data['created'],data['end_date'])
    cur.execute(sql,sql_data)
    id_event = cur.lastrowid

    for topic in data['topics']:
        sql = "SELECT * FROM event_topic WHERE topic = %s AND id_event = %s"
        sql_data = (topic,id_event)
        cur.execute(sql,sql_data)
        event_topic = cur.fetchone()

        if event_topic is None:
            sql = "SELECT * FROM topic WHERE topic.name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            topic = cur.fetchone()

            if topic is None:
                sql = "INSERT INTO topic (name,id_event) VALUES (%s,%s)"
                sql_data = (topic,id_event)
                cur.execute(sql,sql_data)

            sql = "INSERT INTO event_topic (topic,id_event) VALUES (%s,%s)"
            sql_data = (topic,id_event)
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def insert_event_topics(data: dict):
    con = get_connection()
    cur = con.cursor()
    
    for topic in data['topics']:
        sql = "SELECT * FROM event_topic WHERE topic = %s AND id_event = %s"
        sql_data = (topic,data['id_event'])
        cur.execute(sql,sql_data)
        event_topic = cur.fetchone()

        if event_topic is None:
            sql = "SELECT * FROM topic WHERE topic.name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            topic = cur.fetchone()

            if topic is None:
                sql = "INSERT INTO topic (name,id_event) VALUES (%s,%s)"
                sql_data = (topic,data['id_event'])
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
            sql = "SELECT * FROM event_topic WHERE topic.name = %s"
            sql_data = (topic,)
            cur.execute(sql,sql_data)
            events_with_topic = cur.fetchall()

            if len(events_with_topic) == 1:
                sql = "DELETE FROM topic name = %s AND id_event = %s"
                sql_data = (topic,data['id_event'])
                cur.execute(sql,sql_data)

            sql = "DELETE FROM event_topic WEHRE topic = %s AND id_event = %s"
            sql_data = (topic,data['id_event'])
            cur.execute(sql,sql_data)

    con.commit()
    cur.close()
    con.close()


def select_events():
    con = get_connection()
    cur = con.cursor()
    

def select_events_by_topic():
    con = get_connection()
    cur = con.cursor()
    

def insert_interest_topics():
    con = get_connection()
    cur = con.cursor()

    con.commit()


def insert_user():
    con = get_connection()
    cur = con.cursor()

    con.commit()


def user_already_logged():
    con = get_connection()
    cur = con.cursor()
    

def user_exists():
    con = get_connection()
    cur = con.cursor()
    

def login_user():
    con = get_connection()
    cur = con.cursor()

    con.commit()


def logout_user():
    con = get_connection()
    cur = con.cursor()

    con.commit()