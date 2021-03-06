DROP DATABASE IF EXISTS intellirndb;
DROP USER IF EXISTS 'intellirnapp'@'%';

CREATE DATABASE IF NOT EXISTS intellirndb;
CREATE USER IF NOT EXISTS 'intellirnapp'@'%' IDENTIFIED BY 'intellirnpassword';
GRANT ALL ON intellirndb.* TO 'intellirnapp'@'%';

USE intellirndb;

CREATE TABLE survey (
    id BIGINT NOT NULL AUTO_INCREMENT,
    survey_url VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    description VARCHAR(255),
    image_url VARCHAR(255),
    creation_date date,
    last_update_date date,
    PRIMARY KEY (id)
);

CREATE TABLE question (
    id BIGINT NOT NULL,
    survey_id BIGINT NOT NULL,
    content VARCHAR(255),
    stem VARCHAR(255),
    next_url VARCHAR(255) DEFAULT NULL,
    uid BIGINT,
    PRIMARY KEY (survey_id, id),
    CONSTRAINT fk_question_survey FOREIGN KEY (survey_id) REFERENCES survey (id) ON DELETE CASCADE
);

create table category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    category_url VARCHAR(255) NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE education (
    id BIGINT NOT NULL AUTO_INCREMENT,
    education_url VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    description MEDIUMTEXT,
    image_url VARCHAR(255),
    object_url VARCHAR(255),
    creation_date DATE,
    last_update_date DATE,
    link_type varchar(255),
    category_id bigint not null,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE admin (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    middle_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE contact_us (
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR(255),
   email VARCHAR(255),
   subject VARCHAR(255),
   message mediumtext,
   time_received DATETIME(6),
   is_read bit(1),
   PRIMARY KEY (id)
);


INSERT INTO survey (survey_url,title,description,image_url,creation_date,last_update_date) VALUES
("pump","Pump","Take a survey for your pump",NULL,"2022-02-23","2022-02-25"),
("iv-site","IV Site","Take a survey for your IV site",NULL,"2022-02-24","2022-02-24");


INSERT INTO question (id,survey_id,content,stem,next_url,uid) VALUES
("1","1","Are you having a problem with your pump?","Are you having a problem with your pump?",NULL,NULL),
("2","1","Is your pump beeping?","Yes",NULL,"1"),
("3","1","Press SILENCE","Yes",NULL,"2"),
("4","1","No actions needed!","No",NULL,"1"),
("5","1","What does your pump say?","No",NULL,"2"),
("6","1","Is there a kink or blockage in your tubing (given instructions on what to look for)?","DISTAL OCCLUSION",NULL,"5"),
("7","1","Remove Kink","Yes",NULL,"6"),
("8","1","Is there a problem with your IV site (given instructions on how to assess IV site)?","No",NULL,"6"),
("9","1","Call clinic nurses at ### to arrange for new IV site","Yes",NULL,"8"),
("10","1","Then we also do not know what is the problem","No",NULL,"8"),
("1","2","Are you concerned about your IV site","Are you concerned about your IV site",NULL,NULL),
("2","2","At your IV site, do you have pain","YES",NULL,"1"),
("3","2","No problem","NO",NULL,"1"),
("4","2","Press STOP/START button","YES",NULL,"2"),
("5","2","At your IV site, do you have swelling","NO",NULL,"2"),
("6","2","Press STOP/START button","YES",NULL,"5"),
("7","2","At your IV site, do you have redness","NO",NULL,"5"),
("8","2","Press STOP/START","YES",NULL,"7"),
("9","2","At your IV site, do you have leaking","NO",NULL,"7"),
("10","2","Press STOP/START","YES",NULL,"9"),
("11","2","End","NO",NULL,"9");


INSERT INTO category (category_url,category_name) VALUES
("iv-site","IV Site");


INSERT INTO education (education_url,title,description,image_url,object_url,link_type,category_id,creation_date,last_update_date) VALUES
("how-to-deal-with-allergic-reaction","A super informative article","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc sed velit dignissim sodales. Tristique magna sit amet purus gravida quis blandit. Fermentum posuere urna nec tincidunt praesent semper feugiat nibh sed. Porttitor lacus luctus accumsan tortor posuere ac ut. Augue eget arcu dictum varius duis at consectetur lorem. Facilisis magna etiam tempor orci eu lobortis elementum nibh tellus. Amet consectetur adipiscing elit pellentesque habitant morbi tristique senectus. Cras adipiscing enim eu turpis egestas pretium. Aliquam vestibulum morbi blandit cursus risus at ultrices mi tempus.

At ultrices mi tempus imperdiet. Euismod lacinia at quis risus sed. Consectetur libero id faucibus nisl. Ullamcorper eget nulla facilisi etiam dignissim diam quis enim. Lobortis mattis aliquam faucibus purus in massa tempor nec feugiat. Molestie at elementum eu facilisis sed. Aliquet sagittis id consectetur purus ut faucibus pulvinar elementum. Habitant morbi tristique senectus et netus. Duis ut diam quam nulla porttitor massa id. Ornare aenean euismod elementum nisi. Pellentesque habitant morbi tristique senectus et. Amet nulla facilisi morbi tempus iaculis. Ipsum dolor sit amet consectetur adipiscing elit pellentesque. Vitae sapien pellentesque habitant morbi tristique.

Neque egestas congue quisque egestas diam in arcu cursus euismod. Cursus euismod quis viverra nibh cras. Dictumst quisque sagittis purus sit amet volutpat consequat mauris nunc. Feugiat vivamus at augue eget arcu dictum. Sed arcu non odio euismod lacinia at quis risus. Magna fringilla urna porttitor rhoncus. Ultrices gravida dictum fusce ut placerat. Mattis molestie a iaculis at erat pellentesque adipiscing commodo elit. Suspendisse potenti nullam ac tortor vitae purus faucibus ornare suspendisse. At elementum eu facilisis sed odio morbi quis commodo odio. Ut ornare lectus sit amet est placerat in. Mi proin sed libero enim sed faucibus turpis in.

Et netus et malesuada fames ac turpis egestas maecenas. Mattis aliquam faucibus purus in massa tempor. Nulla at volutpat diam ut venenatis tellus in. Dui nunc mattis enim ut tellus elementum. Dis parturient montes nascetur ridiculus. Non quam lacus suspendisse faucibus interdum. Elementum curabitur vitae nunc sed velit dignissim sodales ut. Duis ut diam quam nulla. Arcu risus quis varius quam quisque id. Habitasse platea dictumst vestibulum rhoncus. Ut lectus arcu bibendum at. Pharetra sit amet aliquam id diam maecenas. Donec adipiscing tristique risus nec feugiat in. Nulla at volutpat diam ut venenatis tellus. Cras fermentum odio eu feugiat pretium. Venenatis cras sed felis eget velit aliquet sagittis id consectetur. Neque vitae tempus quam pellentesque nec. Sapien faucibus et molestie ac. Tempus urna et pharetra pharetra massa massa ultricies mi. Sed ullamcorper morbi tincidunt ornare massa eget.

Diam quis enim lobortis scelerisque fermentum dui. Dui sapien eget mi proin sed libero enim. Aliquam nulla facilisi cras fermentum odio. Sem et tortor consequat id porta. Non arcu risus quis varius quam quisque id diam vel. Consectetur a erat nam at lectus urna duis convallis convallis. Elementum facilisis leo vel fringilla est. Commodo elit at imperdiet dui accumsan. Cursus metus aliquam eleifend mi in nulla posuere sollicitudin aliquam. Odio ut enim blandit volutpat maecenas volutpat. Eget nunc scelerisque viverra mauris. Faucibus vitae aliquet nec ullamcorper sit amet risus nullam eget. Ac tortor dignissim convallis aenean.","https://image.shutterstock.com/z/stock-vector-seasonal-allergy-sick-symptoms-problem-infographic-vector-flat-cartoon-illustration-icon-design-789355267.jpg","https://www.youtube.com/watch?v=huRybKL4Hhk","YouTube","1","2022-02-23","2022-02-23");


INSERT INTO admin (first_name,middle_name,last_name,email,password) VALUES
("Erin",NULL,"Bugbee","epbugbee@gmail.com","$2a$10$P.e/dnTzsoNYPJ6pTbItI.URPRkQb/wgAgOvhY0qp6hnKCZjBkajK"),
("Suzanne",NULL,"Burgman","suzburgman@gmail.com","$2a$10$WUcW5GJTxZNZLsBjlqoDDexb9VEEBKzl9mGMdfiShdG93rJiKQQ3C");


INSERT INTO contact_us (name,email,subject,message,time_received,is_read) VALUES
("John Doe","john.doe@gmail.com","Swelling at IV site","I have gotten swelling at my IV site. Please help!!!","2022-02-23 00:00:00",0),
("Adams","adams@gmail.com","My pump is not working","My pump is not working, please help","2022-02-24 00:00:00",0);
