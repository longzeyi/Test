Test
====
CREATE TABLE `tb_positon_detail` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `trade_id` int(10) unsigned NOT NULL,
  `instrument_id` varchar(10) NOT NULL,
  `direction` bit(1) NOT NULL,
  `volume` smallint(5) unsigned NOT NULL,
  `price` decimal(10,0) unsigned NOT NULL,
  `open_date` varchar(8) NOT NULL,
  `trading_day` varchar(8) NOT NULL,
  `exch_margin` decimal(10,0) DEFAULT NULL,
  `margin_rate` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8