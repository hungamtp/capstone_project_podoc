#ROLE TABLE

Insert into Role values (1,"USER");
Insert into Role values (2,"ADMIN");
Insert into Role values (3,"FACTORY");

#HIBERNATE TABLE
Insert into hibernate_sequence values(1);

INSERT INTO `category` VALUES (1,'1',0,'Áo thun'),(2,'1',0,'Áo ba lỗ'),(3,'1',0,'Áo dài tay'),(4,'1',0,'Áo hoodies');

INSERT INTO `color` VALUES (1,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fblack.webp?alt=media&token=00cc21bc-66d5-48b0-809c-b63e845eff5a','Trắng'),(2,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fwhite.png?alt=media&token=8239b14a-151b-4726-a9d1-1334eaeb82e3','Đen'),(3,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fred.jpg?alt=media&token=4ad0c9de-27cd-4b71-9738-4fc3813b6d0d','Đỏ'),(4,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fyellow.png?alt=media&token=5b62f057-36a7-46be-9897-72fddaf39214','Vàng'),(5,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fhinh-nen-mau-tim-don-gian.jpg?alt=media&token=f2704e0d-ab41-4a14-b7a8-878da10ff991','Tím'),(6,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fblue.jpg?alt=media&token=908682cc-bbfb-4b37-8857-5a3266c0d4eb','Xanh dương'),(7,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fgreen.jpg?alt=media&token=8ccaa0b7-cedf-487d-a655-6890758e8540','Xanh lá cây'),(8,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fpink.jpg?alt=media&token=b9a91f1b-739f-4123-b5cf-6624d507669e','Hồng'),(9,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/color%2Fgray.png?alt=media&token=e42488b4-7fd0-4086-98c4-7e9a9f156fa5','Xám');

INSERT INTO `user` VALUES (4,'Duy',_binary '\0','Nguyen','ACTIVE'),(9,'Hiếu',_binary '\0','Nguyễn Minh','INACTIVE'),(11,'Sang',_binary '\0','Vũ Viết','ACTIVE'),(13,'Hưng',_binary '\0','Nguyễn Bá','ACTIVE'),(15,'Dương',_binary '\0','Nguyễn Hữu','ACTIVE'),(17,'Huy',_binary '\0','Đinh Quang','ACTIVE'),(19,'Duy',_binary '\0','Nguyễn Khánh','ACTIVE'),(21,'Son',_binary '\0','Tran Thanh','ACTIVE'),(26,'Duy',_binary '\0','Nguyen','ACTIVE'),(57,'Nguyen',_binary '\0','Thang','ACTIVE'),(59,'Dung',_binary '\0','Nguyen','ACTIVE'),(61,'vu',_binary '\0','sang','ACTIVE');

INSERT INTO `credential` VALUES (5,'2022-06-02','2022-06-02','Huyện U Minh','duyuminh41@gmail.com',NULL,'$2a$10$fmnBIC/jSADYvHDtszvG0uFjnsEex5YR2s1/4MSWkxuJtd6sVbab.','0818258855',NULL,1,4),(10,'2022-06-03','2022-06-03','Chung cư Valéo','hieuthomnghiep@gmail.com',NULL,'$2a$10$67cVi3HFFf3.QHE3YZNcXuGXySV7P16ZGBDHf/psz8SC3tLtNrzuK','0818258855',NULL,1,9),(12,'2022-06-03','2022-06-03','Trần Thị Cờ','sangne@gmail.com',NULL,'$2a$10$0v/.p7slyPHelyPj8zJ9DuKNwVJ6w0wcrFydnyfUhJNt0nwQi5n2.','0818258855',NULL,3,11),(14,'2022-06-03','2022-06-03','Chung cu valéo','hungne@gmail.com',NULL,'$2a$10$V.XTfogjAbkrKGbZBb6mve20Rx9yyBmXmF1lMscEIm5qs2Ew4sO2O','0818258855',NULL,1,13),(16,'2022-06-03','2022-06-03','TP. Thủ Đức','Duongne@gmail.com',NULL,'$2a$10$PLsQ0UCjbQ4PwX3NMwjTHuWi8Prz6nwN86q9eFOFXOH/Z4mGpp0s2','0818258855',NULL,3,15),(18,'2022-06-03','2022-06-03','Xa Lộ Hà Nội','Huyne@gmail.com',NULL,'$2a$10$m8uPZRQ2B78bfh9TmnRR7OJRjRnbmH9mdMnd.L3pwNv4sAiuvFiPu','0818258855',NULL,1,17),(20,'2022-06-03','2022-06-03','Vinhome grand park Q9','duyne@gmail.com',NULL,'$2a$10$/eiFt8gbuaUq0SngljG6nuXhOPjvqsCRkfr0NOY8npAe9N63oeJfO','0818258855',NULL,2,19),(22,'2022-06-03','2022-06-03','Vinhome grand park Q9','sonne@gmail.com',NULL,'$2a$10$tCV/lVrjzLyyjYBp3XLEY.mYVVa6rzzMwsC2YSAH4gVdzKTAL4f4m','0818258855',NULL,1,21),(27,'2022-06-03','2022-06-03','Vinhome grand park Q9','duyuminh4441@gmail.com',NULL,'$2a$10$R.9b61o0/1yZ/iDH36ZQQ.oTDtgAnxnqulHc7ZnQfVAVWeuoDJRAS','0818258855',NULL,1,26),(58,'2022-06-08','2022-06-08','1234567890','duyne123455@gmail.com',NULL,'$2a$10$qxeCjYc4zU9fu0muzrbEVeOaF0oBqONqQ2rcH5lpN7uHrwq.Oxlvu','097104275',NULL,3,57),(60,'2022-06-08','2022-06-08','Chung cu valéo','dung123@gmail.com',NULL,'$2a$10$jkbmwK.ZWTOCaqX/v9QJ0OzgNZc9ti4GBWMcpa2ZkhTzzBjmyMS5i','097104275',NULL,1,59),(62,'2022-06-08','2022-06-08','1234567890','vuvietsangadmin10a9@gmail.com',NULL,'$2a$10$QvskdTmylaPLJcDCnhyc1enr76O1CSx9kEjPgTsRZOnXDZPT14Ie2','097104275',NULL,2,61);

INSERT INTO capstone_pod.factory (id,is_collaborating,location,name) VALUES
                                                                         (1,0,'HCM','SASEU'),
                                                                         (2,0,'HCM','NAP'),
                                                                         (3,0,'HCM','KMS'),
                                                                         (4,0,'HCM','KNS');

INSERT INTO `product` VALUES (1,'2022-06-02','2022-06-02','rất chi là đẹp luôn',_binary '\0',true,'dài tay',1),
                             (6,'2022-06-02','2022-06-02','rất chi là đẹp luôn',_binary '\0',true,'áo thun',1),
                             (23,'2022-06-03','2022-06-03','rất chi là đẹp luôn',_binary '\0',true,'T-shirt',1),
                             (28,'2022-06-03','2022-06-03','rất chi là đẹp luôn',_binary '\0',true,'Áo dài tay trắng',3),
                             (31,'2022-06-03','2022-06-03','rất chi là đẹp luôn',_binary '\0',true,'Áo thun trắng',1),
                             (34,'2022-06-03','2022-06-03','rất chi là đẹp luôn',_binary '\0',true,'Áo thun xám',1),
                             (37,'2022-06-03','2022-06-03','rất chi là đẹp luôn',_binary '\0',true,'Áo ba lỗ trắng',2),
                             (42,'2022-06-08','2022-06-08','rất chi là đẹp luôn',_binary '\0',true,'Áo ba lỗ',2),
                             (45,'2022-06-08','2022-06-08','rất chi là đẹp luôn',_binary '\0',true,'Áo thun ba lỗ ',2),
                             (48,'2022-06-08','2022-06-08','rất chi là đẹp luôn',_binary '\0',true,'Áo thun trắng trơn ',1),
                             (51,'2022-06-08','2022-06-08','rất chi là đẹp luôn',_binary '\0',true,'Áo thun ',1),
                             (54,'2022-06-08','2022-06-08','rất chi là đẹp luôn',_binary '\0',true,'Áo thun vải ',1);

INSERT INTO capstone_pod.price_by_factory (id,price,discount_id,factory_id,product_id) VALUES
                 (1,50.0,NULL,1,1),
                 (2,50.0,NULL,1,6),
                 (3,50.0,NULL,1,23),
                 (4,50.0,NULL,1,28),
                 (5,50.0,NULL,1,34),
                 (6,50.0,NULL,1,37),
                 (7,50.0,NULL,1,42),
                 (8,50.0,NULL,1,45),
                 (9,50.0,NULL,1,48),
                 (10,50.0,NULL,1,51),
                 (11,50.0,NULL,1,54),
                 (12,50.0,NULL,1,31);

    INSERT INTO capstone_pod.product_blue_print (id, frame_image, place_holder_height, place_holder_width, `position`, product_id)
    VALUES(1, '1', 1.0, 1.0, 'Back', 6),(2, '2', 2.0, 2.0, 'Front', 28);

INSERT INTO `product_images` VALUES (2,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',1),(3,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',1),(7,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',6),(8,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',6),(24,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',23),(25,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533',23),(29,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/dai-tay-trang-mat-truoc.png?alt=media&token=34310edc-88f3-407b-b8b9-36a109abf81b',28),(30,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/dai-tay-trang-mat-sau.png?alt=media&token=d8382cca-1aad-4bd6-82c7-ba4aa622e71f',28),(32,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-truoc.png?alt=media&token=9f25a0e1-9dab-472a-93e6-e9a6ca92a2a0',31),(33,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-sau.png?alt=media&token=263c173d-ae24-48c8-b35d-d0864bad71c0',31),(35,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-xam-mat-truoc.png?alt=media&token=0c80d4a2-8943-4325-8816-89b068fb40c3',34),(36,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-xam-mat-sau.png?alt=media&token=0625c7a1-9617-4de7-9fe3-a4cdf81c38db',34),(38,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-truoc.png?alt=media&token=cfcb1a55-47f7-4a1c-80e7-c74beb351bd7',37),(39,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-sau.png?alt=media&token=5ea98a33-cb61-417f-87d2-d6536823e3e8',37),(40,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-1.png?alt=media&token=ca3cee69-3f76-426e-88af-2463ac40e646',37),(41,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-2.png?alt=media&token=931b4fdf-3c76-4128-8d14-c59556b7c3cc',37),(43,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-truoc.png?alt=media&token=cfcb1a55-47f7-4a1c-80e7-c74beb351bd7',42),(44,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-sau.png?alt=media&token=5ea98a33-cb61-417f-87d2-d6536823e3e8',42),(46,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-truoc.png?alt=media&token=cfcb1a55-47f7-4a1c-80e7-c74beb351bd7',45),(47,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/aobalo-trang-mat-sau.png?alt=media&token=5ea98a33-cb61-417f-87d2-d6536823e3e8',45),(49,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-truoc.png?alt=media&token=9f25a0e1-9dab-472a-93e6-e9a6ca92a2a0',48),(50,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-sau.png?alt=media&token=263c173d-ae24-48c8-b35d-d0864bad71c0',48),(52,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-truoc.png?alt=media&token=9f25a0e1-9dab-472a-93e6-e9a6ca92a2a0',51),(53,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-sau.png?alt=media&token=263c173d-ae24-48c8-b35d-d0864bad71c0',51),(55,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-truoc.png?alt=media&token=9f25a0e1-9dab-472a-93e6-e9a6ca92a2a0',54),(56,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/thun-trang-mat-sau.png?alt=media&token=263c173d-ae24-48c8-b35d-d0864bad71c0',54);


INSERT INTO `size` VALUES (1,'XS'),(2,'S'),(3,'M'),(4,'L'),(5,'XL'),(6,'XXL'),(7,'XXXL');

INSERT INTO `size_color` VALUES (1,1,28,1),(2,2,6,2),(3,1,31,3),(4,1,37,3),(34,9,34,3);

INSERT INTO `size_color_by_factory` VALUES (1,100,1,1),(2,100,2,2);

INSERT INTO `tag` VALUES (1,0,'Bán chạy'),(2,0,'Mới'),(3,0,'Trending'),(4,0,'Ưu đãi');

INSERT INTO `product_tag` VALUES (1,54,1),(2,1,1),(3,23,1),(4,28,1),(5,31,1),(6,34,1),(7,37,1),(8,42,1);

INSERT INTO `designed_product`  VALUES
                                    (2,'2022-06-02','2022-06-02',50000.0,'ao ne',0,51,4),
                                    (3,'2022-06-02','2022-06-10',50000.0,'ao thun',0,6,9),
                                    (4,'2022-06-02','2022-06-09',50000.0,'Áo dài tay trắng',0,23,11),
                                    (5,'2022-06-02','2022-06-08',50000.0,'Áo thun trắng',0,28,13),
                                    (6,'2022-06-02','2022-06-07',50000.0,'Áo thun xám',0,31,15),
                                    (7,'2022-06-02','2022-06-06',50000.0,'Áo ba lỗ trắng',0,37,17),
                                    (8,'2022-06-02','2022-06-05',50000.0,'Áo ba lỗ',0,42,19),
                                    (9,'2022-06-02','2022-06-04',50000.0,'Áo thun ba lỗ ',0,45,21),
                                    (10,'2022-06-02','2022-06-03',50000.0,'Áo thun trắng trơn',0,48,26);

INSERT INTO `image_preview`  VALUES
                                    (1 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",2 ),
                                    (2 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",3 ),
                                    (3 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",4 ),
                                    (4 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",5 ),
                                    (5 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",6 ),
                                    (6 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",7 ),
                                    (7 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",8 ),
                                    (8 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",9 ),
                                    (9 ,'https://firebasestorage.googleapis.com/v0/b/assignment1-302217.appspot.com/o/4-0.jpg?alt=media&token=5ce8f57a-65ef-411a-b529-4e098c84e533' , "front",10 );

INSERT INTO `rating`
VALUES (1, 'Good', '2000-06-02', 5, 2, 4),
       (2, 'Bad', '2000-06-02', 2, 2, 4),
       (3, 'Good', '2000-06-02', 5, 2, 4),
       (4, 'Good', '2000-06-02', 5, 2, 4),
       (5, 'Good', '2000-06-02', 5, 3, 4),
       (6, 'Good', '2000-06-02', 5, 3, 4),
       (7, 'Good', '2000-06-02', 5, 3, 4),
       (8, 'Good', '2000-06-02', 5, 3, 4);
INSERT INTO capstone_pod.blue_print (id,frame_image,`position`,designed_product_id) VALUES
                                                                                        (11,'1','1',2),
                                                                                        (2,'1','1',3),
                                                                                        (3,'1','1',4),
                                                                                        (4,'1','1',5),
                                                                                        (5,'1','1',6),
                                                                                        (6,'1','1',7),
                                                                                        (7,'1','1',8),
                                                                                        (8,'1','1',9),
                                                                                        (9,'1','1',10);

INSERT INTO capstone_pod.design_info (id,height,left_position,name,rotate,scales,src,top_position,types,width,x,y,blue_print_id) VALUES
                                                                                                                                     (2,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,2),
                                                                                                                                     (3,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,3),
                                                                                                                                     (4,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,4),
                                                                                                                                     (5,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,5),
                                                                                                                                     (6,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,6),
                                                                                                                                     (7,1.0,1.0,'ao',1.0,1.0,'ao',1.0,'image/jpeg',1.0,1.0,1.0,7);

INSERT INTO `placeholder` VALUES (7,100,1,11),(2,100,2,2),(3,100,1,3),(4,100,2,4),(5,100,1,5),(6,100,2,6);

INSERT INTO `designed_product_tag`  VALUES
                                        (1,10,1),
                                        (2,2,2),
                                        (3,3,3),
                                        (4,4,4),
                                        (5,5,1),
                                        (6,6,2),
                                        (7,7,3);
