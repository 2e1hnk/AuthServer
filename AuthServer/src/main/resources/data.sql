-- In all cases client_secret = 'secret'
INSERT IGNORE INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('fooClientIdPassword', '$2a$10$qUsfIje0baElKv7AFxN8RuGjhk016t5ldDz3XYQU.HRvJZjI7fDem', 'foo,read,write', 'password,authorization_code,refresh_token,client_credentials',"http://oauth-client:8080/ui-thymeleaf/login/oauth2/code/custom,http://localhost:8080/login/oauth2/code/custom,http://localhost:8080/ui-thymeleaf/login/oauth2/code/custom,http://localhost:8080/authorize/oauth2/code/bael", null, 36000, 36000, null, true),
	('sampleClientId', '$2a$10$qUsfIje0baElKv7AFxN8RuGjhk016t5ldDz3XYQU.HRvJZjI7fDem', 'read,write,foo,bar', 'implicit', "http://localhost:8083/,http://localhost:8086/", null, 36000, 36000, null, false),
	('barClientIdPassword', '$2a$10$qUsfIje0baElKv7AFxN8RuGjhk016t5ldDz3XYQU.HRvJZjI7fDem', 'bar,read,write', 'password,authorization_code,refresh_token', "http://www.example.com", null, 36000, 36000, null, true);


-- Default admin password is 'nimda' - CHANGE IT!
INSERT IGNORE INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$9lcB.3p1uBNNAr.W6mcKHuN6Sq0WrbhNG9a5dXtvX848yqJOuQEPS', true);

INSERT IGNORE INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');