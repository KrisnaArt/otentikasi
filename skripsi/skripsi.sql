-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 12, 2021 at 05:56 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `skripsi`
--
CREATE DATABASE IF NOT EXISTS `skripsi` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `skripsi`;

-- --------------------------------------------------------

--
-- Table structure for table `foto_user`
--

DROP TABLE IF EXISTS `foto_user`;
CREATE TABLE `foto_user` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `foto_wajah` varchar(100) DEFAULT NULL,
  `foto_ktp` varchar(100) DEFAULT NULL,
  `foto_baru` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `foto_user`
--

INSERT INTO `foto_user` (`id`, `username`, `email`, `password`, `foto_wajah`, `foto_ktp`, `foto_baru`) VALUES
(1, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(2, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(3, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(4, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(5, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(6, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(7, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(8, '', '', '', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(9, 'asd', 'qwe@asd.com', 'asdfgh', 'https://192.168.1.3/skripsi/images/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/1606657272365.jpg', NULL),
(10, 'asd', 'qwe@asd.com', 'asdfgh', 'https://192.168.1.3/skripsi/images/foto_wajah/1605169849900.JPG', 'https://192.168.1.3/skripsi/images/foto_ktp/1606657272365.jpg', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `foto_user`
--
ALTER TABLE `foto_user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `foto_user`
--
ALTER TABLE `foto_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
