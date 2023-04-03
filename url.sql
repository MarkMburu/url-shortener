DROP TABLE IF EXISTS `url`;
CREATE TABLE `url` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_url` varchar(2048) DEFAULT NULL,
  `short_url` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

