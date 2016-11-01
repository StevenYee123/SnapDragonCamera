# Dragonboard 410c Installation Guide for Linux and Android

This Users Guide provides a general overview for getting started with Linux and Android installations supporting the [DragonBoard 410c](https://www.96boards.org/products/ce/dragonboard410c/).   There are two primary ways to install software onto the DragonBoard 410c.  
First and simplest, an installer image can be downloaded and put on an SD Card.  This can then be placed into the board and will automatically boot and install either an Android or Linux Desktop into the board.  See the [Installing Image using an SD Card Image](#installing-image-using-an-sd-card-image) section to use this method.   
Second, fastboot is supported by the board and can be used for installs.  This is for advanced users who are most likely modifying/customizing source code and will need to download such updates to the board for test/execution. See [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot) to use this method.  When installing Debian using Fastboot, note that the default flow is for installing a Developer(console-only) image.  There is an option in that section, however, to install an LXDE desktop Debian image instead.  When installing Debian using the SD Card method, LXDE desktop install is currently the only option, and there is not a Console-only choice; Fastboot is the only way to attain a Console-only Debian install.
This document also contains additional supporting sections such as [Setting Up the UART console](#setting-up-the-uart-console) and [creating and installing a rescue image](#create--install-a-rescue-image) in the case that the board experiences an irrecoverable error and no longer functions.    

**Table of Contents**

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Install Android or Debian from an SD Card](#install-android-or-debian-from-an-sd-card)
  - [Overview](#overview)
  - [Installing Image using an SD Card Image](#installing-image-using-an-sd-card-image)
  - [Flashing the SD Card Image to the DB410c](#flashing-the-sd-card-image-to-the-db410c)
- [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot)
  - [Install Linaro/Debian release](#install-linarodebian-release)
    - [Flash the bootloader into on-board eMMC](#flash-the-bootloader-into-on-board-emmc)
    - [Flash the Linaro/Debian release](#flash-the-linarodebian-release)
  - [Install Android release](#install-android-release)
    - [Flash the bootloader into on-board eMMC](#flash-the-bootloader-into-on-board-emmc-1)
    - [Flash the Android release](#flash-the-android-release)
- [Understanding Build Folder Layout](#understanding-build-folder-layout)
  - [Bootloaders and Rescue Image](#bootloaders-and-rescue-image)
  - [Debian Images](#debian-images)
  - [Android Images](#android-images)
  - [Snapshots Folders](#snapshots-folders)
- [Switching between Android and Linaro Linux releases](#switching-between-android-and-linaro-linux-releases)
- [Creating a Kernel Image from Source](#creating-a-kernel-image-from-source)
- [Optional Boot Method](#optional-boot-method)
- [Replacing the Bootloader](#replacing-the-bootloader)
- [Create / Install a Rescue Image](#create--install-a-rescue-image)
  - [Download the Rescue Image](#download-the-rescue-image)
  - [Create an SD Card Rescue Image](#create-an-sd-card-rescue-image)
  - [Boot Rescue Image on DB410c](#boot-rescue-image-on-db410c)
- [Setting Up the UART console](#setting-up-the-uart-console)
- [Description of LED’s and Connectors](#description-of-led%E2%80%99s-and-connectors)
  - [Connectors / Switch](#connectors--switch)
  - [LEDs](#leds)
    - [Debian Images](#debian-images-1)
    - [Android Images](#android-images-1)
- [Using USB](#using-usb)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Install Android or Debian from an SD Card

### Overview

This section provides information about the installation of pre-built
Android or Debian SD card images onto the DragonBoard™ 410c. This
installation method targets the following user/developer use-cases:

- Just getting familiar with the DragonBoard™ 410c
- A user that wants to extend the Debian or Android installation by
  adding new packages or applications. This user is using the
  DragonBoard™ 410c as an end product, like a lightweight desktop,
  etc.

For a user / developer that plans to be extending the functionality
through adding his or her own code and will be downloading many
iterative experimental versions of self-compiled OS’s, it is recommended
to go directly to the [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot)  section of this document.

This SD card image includes a flashing program (herein referred to as
”FT” for “Flashing Tool”) that will be executed from the DragonBoard™
410c (herein referred to interchangeably with “the DB410c”), and
doesn't require any specific equipment on the developers Linux-based
development system (herein referred to as the “host PC”).

The FT will execute from the DB410c and provide a very simple way to
install target software once the SD Card has been created as shown in
the next section. It can be configured to install Android or Debian
into the target eMMC memory, subsequently allowing the target to
directly boot into the OS installed, and no longer requiring the SD
Card.

Once the SD card image is written to a microSD card on your host PC, you
simply need to insert the microSD card in the microSD Card slot on the
DragonBoard ™ 410c, setup the board to boot from SD (instead of eMMC),
and boot it by turning on the power. The following three sections walk
a developer through this process.

### Installing Image using an SD Card Image

This section builds the SD Card image for the DB410c. The steps in this
section are executed entirely from the host PC. It is assumed that the
developer is using a Linux development host.  

Two SD installation images are released at Linaro, one for each of the
following OS’s: Android and Debian.

The steps to create the SD Card Image are as follows:

**Step 1: Place a microSD Card into the Host PC**

The microSD Card must be a minimum of 4 GB and will be erased, so make
sure no valuable files you wish to retain are on the microSD Card.

**Step 2: Download desired SD Card Image**

Click on the link below to begin download of the latest SD-Card install image: 
- [Debian SD Card Install image](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/dragonboard410c_sdcard_install_debian*.zip)
- [Android SD Card Install image](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/dragonboard410c_sdcard_install_android*.zip)

- It’s recommended to download the associated MD5SUMs for the
  build as well and verify this against the downloaded zip image using
  the md5sum command on the command line. To become familiar with
  md5sum command usage, type “man md5sum” on the command line of the
  Linux host PC.
  - [Debian MD5SUM](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/MD5SUMS.txt)
  - [Android MD5SUM](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/MD5SUMS)

**Step 3: Unzip the downloaded install file**

Note the directory that the install file was extracted to, as this will
be required in the next step. There are multiple tools to unzip and
extract these archives. In this workflow, “Archive Manager,” available
by default in Ubuntu/Debian Desktop, was used.

**Step 4: Write the SD image onto an SD card**
Note: You can do this on Linux, or on Windows.

***Linux instructions***

The SD card does not need to be formatted in any special way since this
procedure will write the entire image, including the partition
table.

From the command prompt on the host PC, enter the following sequence
of commands:

```shell
# cd to the directory the install file was extracted to
cd <extraction directory>

sudo dd if=db410c_sd_install_YYY.img of=/dev/XXX bs=4M oflag=sync status=noxfer
```

Where:

- YYY can be android or debian
- where XXX is the device name

**Warning:** Executing the sync flag command as outlined above is very
important! Do not skip this step!

**Warning:** Be very careful not to overwrite your hard drive! In most
cases, XXX will be mmcblk0 or sdb. This can be found by using the
following sequence:

1. Make sure the SD Card is not in the host PC
2. From the host PC terminal command line, run the following command:
```shell
   lsblk
```
3. Note the listed blocks such as sda, sdb, etc.
4. Insert the SD Card into the host PC
5. From the host PC terminal command line, run the following command:
```shell
   lsblk
```
6. There will be a new device in the list, this will be your SD Card
   identity (XXX).  It also has a size parameter that should match the size of the SD Card.

***Windows instructions***

1. Download the [Win32DiskImager tool](http://sourceforge.net/projects/win32diskimager/)

2. Open the tool, and click the folder icon to select the appropriate img

![](http://i.imgur.com/JurbrUf.png)

3. Attach your micro SD card (through a USB SD card-reader, if necessary). 
4. Select "write." There may be a warning about corrupting the device. Click "Yes" to proceed.
![](http://i.imgur.com/BwQ6Jid.png)

5. It should show the following image upon successful completion:

![](http://i.imgur.com/vQsSTKX.png)


### Flashing the SD Card Image to the DB410c

Once the image is written on the SD Card per the previous section,
perform the following steps to flash the image into the eMMC memory on
the DragonBoard™ 410c:

- remove the microSD card from the host PC
- remove the power connector from the DB410c and insert the micro SD
  card into the DB410c
- set the S6 switch on the DB410c to: 0-1-0-0 {SD Boot switch set to “ON”}
- plug a USB keyboard and/or mouse into either of the two USB
  connectors on the DB410c
  Note: If no mouse is used, the keys in the parenthesis on the
  installer screen can be used to initiate install commands. For
  example “Install(i)” would be the “i” key on the keyboard. Arrow
  keys may also be used during the command initiation sequences.
- connect an HDMI monitor to the DB410c with an HDMI cable, and power
  on the monitor
- connect the power cable to the DB410c

When the board powers up, it will boot into an installer similar to the
figure below:

![](https://github.com/96boards/documentation/blob/master/ConsumerEdition/DragonBoard-410c/AdditionalDocs/Images/Wiki/image00.jpg)

- Select the image to install and then click on “Install” (or type
  “i”) and the OS will be installed into the eMMC memory on the
  DB410c.

After installation completes, a “Flashing has completed and OS has
installed successfully....” message will appear on the HDMI monitor
connected to the DB410c.

- Remove the SD Card and click “OK” as instructed, and the DB410c will
  reboot into the newly installed OS

Upon completion of the above, the user will now see either an Android or
an Debian Desktop, depending upon which one was installed.

Congratulations! You are now booting your newly installed OS directly
from eMMC on the DragonBoard™ 410c!

## Install Android or Debian Using Fastboot

This section provides an alternative method for installing images onto
the DB410c.  This is for advanced users that plan to be flashing many versions of the OS into the DB410c.

Prerequisites:

- The bootloader on this board supports fastboot. The developer should
  first make sure that fastboot is installed on the host PC. If you
  are using Ubuntu/Debian on your host PC use the following command to
  install fastboot:

```shell
sudo apt-get install android-tools-fastboot
```

- The DB410c must have been imaged with fastboot previously.  There are two ways to do this:
  - by following the procedure outlined in the [Create / Install a Rescue Image](#create--install-a-rescue-image) section of this document
  - by following the procedure in the [Installing Image using an SD Card Image](#installing-image-using-an-sd-card-image) section of this document

### Install Linaro/Debian release

#### Flash the bootloader into on-board eMMC

Download the latest Ubunutu bootloader zip located [**here**](http://builds.96boards.org/releases/dragonboard410c/linaro/rescue/latest/dragonboard410c_bootloader_emmc_linux*.zip)

Flash the eMMC with the bootloader:
- unzip the bootloader that was downloaded in the previous step. Note
  the directory that is it located in.
- assure that a micro USB cable is connected from the micro-USB port
  on the DB410c to the host PC
- assure micro SD Card slot is empty on the DB410c
- set the S6 switch on the DB410c to: 0-0-0-0 {SD Boot set to off}
- power on the DB410c into fastboot mode
  - Press and hold the Vol (-) button on the DB410c (S4)
  - While pressing S4 button, power up the DB410c. It will come up in
  fastboot mode
- from the host PC terminal window, run the following commands:

```shell
# Check to make sure fastboot device connected.  If not resolve
sudo fastboot devices

# cd to the directory the bootloader zip file was extracted
cd <extraction directory>

sudo ./flashall
```

The bootloader is now installed on the DB410c.

#### Flash the Linaro/Debian release

This section assumes the DB410c is still in fastboot mode from the
previous section.  This procedure also assumes that the developer will be using the latest build.

Flash the Linaro/Debian boot image and root file system by
performing the following steps:
- Initiate the download of the latest Debian boot image zip by clicking [**here**](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/boot-linaro-jessie-qcom-snapdragon-arm64*.img.gz)
- Initiate the download of the latest Debian Root File System(RootFS) zip by clicking [**here**](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/linaro-jessie-developer-qcom-snapdragon-arm64*.img.gz)

  Their names will be similar to the following:
  - ./boot-linaro-jessie-qcom-snapdragon-arm64-BUILD#.img.gz
  - ./linaro-jessie-developer-qcom-snapdragon-arm64-BUILD#.img.gz

  Where BUILD# is the date/Build stamp for the downloaded file

**Note:** The procedure above loads the command line debian, denoted by
“developer” in the name.  If the user wishes to load a graphical X
version based on LXDE, click [**here**](http://builds.96boards.org/releases/dragonboard410c/linaro/debian/latest/linaro-jessie-alip-qcom-snapdragon-arm64*.img.gz) and use this file in the place of the above RootFS file for the rest of this install.  This file's name will be similar to the following:

```shell
  linaro-jessie-alip-qcom-snapdragon-arm64-BUILD#.img.gz
```

- Flash the boot image and rootfs to the DB410c by executing the
  following commands from the host PC:

```shell
# Check to make sure fastboot device connected.  If not resolve
sudo fastboot devices

# cd to the directory the boot image and RootFS were extracted
cd <extraction directory>

gunzip *.img.gz
sudo fastboot flash boot boot-linaro-jessie-qcom-snapdragon-arm64-BUILD#.img
sudo fastboot flash rootfs linaro-jessie-developer-qcom-snapdragon-arm64-BUILD#.img
```

**Note:** Replace BUILD# in the above commands with the file-specific date/build stamp. 

Now reboot the DB410c using the following sequence and it will boot to the command prompt:
- Unplug the power to the DB410c
- Unplug the micro USB fastboot cable
- Plug the power back into the DB410c

**Note:** the username and password are both “**linaro**” when the login information is
requested.

### Install Android release

#### Flash the bootloader into on-board eMMC

Download the latest Android bootloader zip located [**here**](http://builds.96boards.org/releases/dragonboard410c/linaro/rescue/latest/dragonboard410c_bootloader_emmc_android*.zip).

Flash the eMMC with the Android Bootloader:
- unzip the bootloader that was downloaded in the previous step. Note
  the directory that is it located in.
- assure that a micro USB cable is connected from the micro-USB port
  on the DB410c to the host PC
- assure micro SD Card slot is empty on the DB410c
- set the S6 switch on the DB410c to: 0-0-0-0 {SD Boot set to off}
- power on the DB410c into fastboot mode
  - Press and hold the Vol (-) button on the DB410c (S4)
  - While pressing S4 button, power up the DB410c. It will come up in
  fastboot mode
- from the host PC terminal window, run the following commands:

```shell
# Check to make sure fastboot device connected. If not resolve
sudo fastboot devices

# cd to the directory the bootloader zip file was extracted
cd <extraction directory>

sudo ./flashall
```
The bootloader is now installed on the DB410c.

#### Flash the Android release

This section assumes the DB410c is still in fastboot mode from the previous section. This procedure also assumes that the developer will be using the latest build.

Flash the Android boot build by performing the following steps:
- Download the following files from the latest build folder to a local folder on the host PC by clicking on the six links below:

     [boot.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/boot.img.tar.xz)  
     [system.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/system.img.tar.xz)  
     [userdata.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/userdata.img.tar.xz)  
     [recovery.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/recovery.img.tar.xz)    
     [persist.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/persist.img.tar.xz)  
     [cache.img.tar.xz](http://builds.96boards.org/releases/dragonboard410c/qualcomm/android/latest/cache.img.tar.xz)  

Now to unzip and flash the files to the DB410c. From the host PC command line, execute the following:

```shell
for f in *.tar.xz; do tar xJf $f; done
sudo fastboot flash boot boot.img
sudo fastboot flash system system.img
sudo fastboot flash userdata userdata.img
sudo fastboot flash recovery recovery.img
sudo fastboot flash persist persist.img
sudo fastboot flash cache cache.img
```

Reboot the DB410c by following the steps below, and it will boot Android:  
- Unplug the power from the DB410c
- Unplug the micro USB cable so that that it will boot into USB Host mode
- plug the power back into the DB410c

Note: This make take a bit of time during the initial bringup. 

Congratulations! You now have an Android experience running on your desktop!

## Understanding Build Folder Layout
When downloading and installing binary images, the procedures in this document provides 
direct links to the latest builds for each component required in the DB410c.  There may be 
times when a developer wants direct control of a component install, however, and would like to instead 
install a specific build version.  This section explains the folder layout so that a developer can 
navigate and download specific versions as opposed to the latest.  These files would then replace
the latest files used in the install procedures in this document.  Note that some of the folders 
contain residual files from builds that are not required to create install images.

The notes below provide this clarification:

### Bootloaders and Rescue Image
Location:  http://builds.96boards.org/releases/dragonboard410c/linaro/rescue

Contains Bootloader and Rescue images.  
Files of interest:
- dragonboard410c_sdcard_rescue-BUILD#.zip             - Rescue image for DB410c
- dragonboard410c_bootloader_emmc_linux-BUILD#.zip     - Linux-specific Bootloader
- dragonboard410c_bootloader_emmc_android-BUILD#.zip   - Android-specific Bootloader
- MD5SUMS.txt - Contains checksums for all the zip files to verify that downloads were not corrupted

### Debian Images
Location:  http://builds.96boards.org/releases/dragonboard410c/linaro/debian

Contains the Debian boot images and rootFS for both developer (console-only) and LXDE builds.  
Also contains the SC Card auto-install file for Debian.
Files of interest:
- dragonboard410c_sdcard_install_debian-BUILD#.zip - SD Card Auto-Install file of fastboot recovery, Linux Bootloader, Debian Boot Image, and LXDE RootFS 
- boot-linaro-jessie-qcom-snapdragon-arm64-BUILD#.img.gz           - Debian Boot Image
- linaro-jessie-developer-qcom-snapdragon-arm64-BUILD#.img.gz      - Console only Debian RootFS
- linaro-jessie-alip-qcom-snapdragon-arm64-BUILD#.img.gz           -  LXDE Desktop RootFS
- MD5SUMS.txt - Contains checksums for all the zip files to verify that downloads were not corrupted

### Android Images
Location:  http://builds.96boards.org/releases/dragonboard410c/qualcomm/android

Contains Android boot images and RootFS for Android install.
Also contains the SD Card auto-install file for Debian.
Files of interest:  
- The six files required for an Android Install  
  - boot.img.tar.xz  
  - system.img.tar.xz  
  - userdata.img.tar.xz  
  - recovery.img.tar.xz  
  - persist.img.tar.xz  
  - cache.img.tar.xz  
- MD5SUMS - Contains checksums for all the zip files to verify that downloads were not corrupted

### Snapshots Folders
Location:  http://builds.96boards.org/snapshots/

So far, this section has described the ./releases folders that contain archived images.  These are characterized by stable and validated releases.  In addition, there is a snapshots directory structure that has builds of the same content in the releases directory structure.  The primary differences are as follows:
* snapshots are created daily (or more) when developers make code updates to the source base. This is an automated build.
* snapshots may or may not be functional.  Functionality nor testing of snapshots is not guaranteed.
* snapshots are not archived.  Older ones will be deleted by the maintainer to prevent an overwhelming amount of data space being used
* when a snapshot is determined to be a candidate for long-term archival, it will be moved into the releases/ folders.
* individual file names and versions between the snapshots/  and the releases/ are identical

Thus, in summary, snapshots are the initial output of the builds.  Once tested and determined to be a candidate for long-term usage/archival, a snapshot is copied to the releases directory.

When a developer may want to use images from snapshots/ instead of releases/:
* when debugging, it may be desirable to attain a snapshot build
* when a question is asked on a forum, the developer supporting an issue may ask a user to use a specific build from snapshots/ to help isolate a problem
* when a developer would like to help validate builds for long-term use, testing of snapshots can be done by such a person.  Participation is welcome and encouraged!

Note: In general, using images from snapshots/ instead of releases/ should be the exception and not the norm.  If the user is not sure, use images from releases/.

## Switching between Android and Linaro Linux releases
It is possible to switch back and forth between the Android and the
Linaro Linux builds. To do so, simply follow the
regular instructions from the [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot) section, and make sure to install the
appropriate bootloader release package, since the partition table used
for both Android and Linux are different.

## Creating a Kernel Image from Source
To create a kernel image from source, follow the instructions found 
[**here**](https://github.com/96boards/documentation/wiki/Dragonboard-410c-Boot-Image).

## Optional Boot Method
Note that it is not mandatory to flash the boot image in the boot
partition. When the platform boots, it will check to see if there is a valid
boot image in the boot partition, and then load it and automatically
boot if it is valid. If the boot partition is not flashed with a valid image, then the
bootloader will boot into fastboot mode. Once the platform is in
fastboot mode, the user can boot a local boot image from the host PC with the following
command:

```shell
# cd to the directory the boot image is located
cd <boot image directory>

fastboot boot boot.img
```

## Replacing the Bootloader
If a boot image has been flashed into the boot partition as described herein, the DB410c will automatically boot when it is powered on. If it is required for any reason to reflash another boot image, there are three options:

1. Follow the instructions to install the boot image in either the Debian or the Android sub-section of the [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot) section of this document.

2. Using the Vol- button (S4 on the DB410c) to force the DB410c into fastboot mode
  - Power down the DB410c
  - Connect a micro USB-USB cable from the micro USB port on the DB410c
   to a USB port on the host PC
  - Press and hold the Vol (-) button on the DB410c (S4)
  - While pressing S4 button, simultaneously power up the DB410c. It will come up in
   fastboot mode
  - From the host PC terminal, type the following to verify a fastboot
   connection has been established:

```shell
   sudo fastboot devices
```

3. On the DB410c, boot and open a terminal. Then from the command line,
   run the following commands to erase the boot partition:

```shell
cat /dev/zero > /dev/disk/by-partlabel/boot
reboot
```
  
**Warning:** perform the above command only on the DB410c target and only if a new boot image is known to be required!

Note: If it is desired to rebuild the kernel instead of using prebuilts,
please check this wiki with instruction to rebuild your own boot image:

[https://github.com/96boards/documentation/wiki/Dragonboard-410c-Boot-Image](https://github.com/96boards/documentation/wiki/Dragonboard-410c-Boot-Image)

## Create / Install a Rescue Image

This section provides the instructions for creating a “rescue image”
that can then be used to install fastboot on the DragonBoard™ 410c.
After this section has been completed, the user will be able to follow
the procedures in the [Install Android or Debian Using Fastboot](#install-android-or-debian-using-fastboot) section to install updated/customized OS’s.

### Download the Rescue Image  

- Download the [**Latest Rescue Image**](http://builds.96boards.org/releases/dragonboard410c/linaro/rescue/latest/dragonboard410c_sdcard_rescue*.zip)

### Create an SD Card Rescue Image  
- Insert the micro SD card, into the host PC (micro SD Card must be at least 1GB)
- Unzip the rescue image just downloaded into the desired directory
- Execute the following commands from the host PC:

```shell
# cd to the directory the rescue image file is located
cd <rescue image directory> 

sudo dd if=db410c_sd_rescue.img of=/dev/XXX bs=4M oflag=sync status=noxfer
```

where XXX is the device name.

**Warning:** Executing the sync flag command as outlined above is very important! Do not skip this step!

**Warning:** Be very careful not to overwrite your hard drive! In most cases, XXX will be mmcblk0 or sdb. This can be found by using the following sequence:  
  - Make sure the SD Card is not in the host PC
  - From the host PC terminal command line, run the following command:
```shell
   lsblk
```

  - Note the listed blocks such as sda, sdb, etc.
  - Insert the SD Card into the host PC
  - From the host PC terminal command line, run the following command:
```shell
   lsblk
```

  - There will be a new device in the list, this will be your SD Card
   identity (XXX) 

You now have an SD card that contains the rescue image and is ready to
plug into the DB410c.

### Boot Rescue Image on DB410c

To boot the micro SD card containing the Boot Rescue image, perform the
following:

- unplug the DB410c power
- Optional: connect the Serial FTDI cable from target to the host,
  and open a serial terminal on the host PC.  Instructions for this
  can be found in the section titled ["Setting Up the UART console"](#setting-up-the-uart-console)
- make sure that the micro USB port cable is plugged into PC
- set the S6 switch to: 0-1-0-0 {“SD Boot” set to “ON” with all other
  switches set to off}
- insert the SD card built in the previous section into the DB410c
- power on the board

This will install the fastboot bootloader into the eMMC, and on the serial
console you will see a message similar to the following (if it is
optionally connected):

```
QC_IMAGE_VERSION_STRING=BOOT.BF.3.0-00261
...
Rescue image for DragonBoard 410c, boot into fastboot mode only
...
fastboot: processing commands
```

The rescue image is now installed and the developer can now use fastboot
to install new OS images.

- Remove the SD card from the DB410c, make sure the micro USB cable is still plugged in, and reboot (power cycle). Fastboot will now be running on the DB410c and the creation of the rescue image is complete.

Note: the SD Boot switch can be left “ON” in many cases if desired. This simply
defines boot order, and if no SD Card, then boot defaults to the eMMC to boot the
newly flashed fastboot.

## Setting Up the UART console

A UART serial console can be optionally connected from a host PC USB port
to UART1, which is available on the DB410c low speed expansion connector
(J8). This allows a developer to bring up a terminal emulator on the
host PC and communicate with the DB410c using a command line interface. This
can be very valuable when performing in depth debugging as well as
using fastboot. The following defines the pins used for UART1 on the J8
connector:

- PIN1 is GND
- PIN11 is TX
- PIN13 is RX

A standard USB TTL FTDI cable, such as Part Number: *TTL-232RG-VREG1V8-WE*, that steps up the 1.8 volts available on the DB410c is required.   If one with the part number highlighted is used, the the pinout is as follows:

- BLACK -> PIN1
- YELLOW -> PIN11
- ORANGE -> PIN13

**Warning:** The FTDI cable above is required to be 1.8 volts (this is
denoted VREG1V8 in the part number above). Damage can occur to your
hardware if the wrong voltage cable is used.

A picture of the FTDI cable connected to the DragonBoard™ 410c is shown
below:

![](https://github.com/96boards/documentation/blob/master/ConsumerEdition/DragonBoard-410c/AdditionalDocs/Images/Wiki/image00.jpg)
Figure 1: FTDI Cable connected to J8 Header on DragonBoard™ 410c

**Warning:** Make sure that the extra wires are not touching the board in
any way. This can cause a short and damage the hardware. It is
recommended to either clip and tape the unused wires on the cable or to
solder a small header onto the end of the cable.

**Warning:** Never install the wires while the DragonBoard™ 410c is plugged
into power or has USB cables plugged into it.

**Warning:** Always wear safety glasses when working with any hardware to
avoid personal injury.

Warning: It is recommended that developers use an ESD compliant
environment when handling the DragonBoard™ 410c to avoid damaging the
electronics on the board.

After the above is completed, the user can perform the following steps
to access the DB410c serial console:

- Connect the USB connector of the FTDI cable to the host PC. This
  assumes that the other end is already connected to the DB410c
- Open a terminal on the host PC
- On the host PC find the full path of the FTDI cable in the /dev/…
  device tree
- On the host PC use a terminal emulator application to bring up the
  console. For purposes of example, these instructions use “screen”
```shell
sudo screen /dev/<path of the FTDI> 115200
```

Note: The default speed of UART1 is 115200 Baud on the DB410c. 

- Once the above has been executed, power on the DB410c and the user
  will see boot sequence text scroll in the console on the host PC
  ending with a Linux command prompt once the DB410c is booted.

## Description of LED’s and Connectors

Throughout this document, references are made to certain board
connectors, headers and switches.  There are also six activity LEDs on
the board. These items are described further in this section

### Connectors / Switch

```
+----------------------+----------------------+--------------------------+
| Board Identifier     | Description          | Behavior                 |
+----------------------+----------------------+--------------------------+
| J8                   | Low Speed Connector  | This connector contains  |
|                      |                      | low speed peripheral     |
|                      |                      | signals just as UART and |
|                      |                      | GPIO. In context of      |
|                      |                      | this document, we use it |
|                      |                      | for connecting the UART. |
+----------------------+----------------------+--------------------------+
| S6                   | 4 channel Dip Switch | Located on the back of   |
|                      |                      | the board, this Switch   |
|                      |                      | provides some manual     |
|                      |                      | configuration settings.  |
|                      |                      | In context of this       |
|                      |                      | document, it is used to  |
|                      |                      | select boot order (SD    |
|                      |                      | Card first or eMMC       |
|                      |                      | first) OTG modes.        |
|                      |                      |                          |
+----------------------+----------------------+--------------------------+
```

### LEDs

Located by the USB ports are a series of LEDs used to provide
information to the user. Their usage is defined as follows:

#### Debian Images
When Debian-based images are installed, the following table defines the LED usage/behaviors.
```
+----------------------+----------------------+--------------------------+
| LED Board Identifier | Description          | Behavior                 |
+----------------------+----------------------+--------------------------+
| User LED 1           | Heartbeat            | Green: This LED is       |
|                      |                      | should always be         |
|                      |                      | blinking about once a    |
|                      |                      | second. If solid off or  |
|                      |                      | solid on, the board is   |
|                      |                      | not executing correctly  |
+----------------------+----------------------+--------------------------+
| User LED 2           | eMMC                 | Green: This LED blinks   |
|                      |                      | during accesses to eMMC  |
+----------------------+----------------------+--------------------------+
| User LED 3           | SD                   | Green: This LED blinks   |
|                      |                      | during accesses to SD    |
|                      |                      | Card                     |
+----------------------+----------------------+--------------------------+
| User LED 4           | currently unassigned | N/A                      |
+----------------------+----------------------+--------------------------+
| Wifi                 | Wifi                 | Yellow: This LED blinks  |
|                      |                      | during network accesses  |
|                      |                      | over Wifi                |
+----------------------+----------------------+--------------------------+
| BT                   | Bluetooth            | Yellow: This LED blinks  |
|                      |                      | when Bluetooth is being  |
|                      |                      | used                     |
+----------------------+----------------------+--------------------------+
```

#### Android Images
When Android-based images are installed, the following table defines the LED usage/behaviors.

```
+----------------------+----------------------+--------------------------+
| LED Board Identifier | Description          | Behavior                 |
+----------------------+----------------------+--------------------------+
| User LED 1           | currently unassigned | Green:                   |
+----------------------+----------------------+--------------------------+
| User LED 2           | currently unassigned | Green:                   |
|                      |                      |                          |
+----------------------+----------------------+--------------------------+
| User LED 3           | currently unassigned | Green:                   |
|                      |                      |                          |
|                      |                      |                          |
+----------------------+----------------------+--------------------------+
| User LED 4           | Boot                 | This LED illuminates at  |
|                      |                      | at the start of boot     |
|                      |                      | and turns of after       |
|                      |                      | completion of boot.      |
+----------------------+----------------------+--------------------------+
| Wifi                 | Wifi                 | Yellow: TDB              |
+----------------------+----------------------+--------------------------+
| BT                   | Bluetooth            | Yellow: TBD              |
+----------------------+----------------------+--------------------------+
```

## Using USB

On this board, both USB Host and USB On-The-Go (OTG) are available,
however they cannot be used simultaneously. OTG is used by the
bootloader for fastboot. USB Host is also supported.

To use USB OTG:

- plug a micro USB cable in J4 connector and the other end of the cable plugged into the host PC. Note that if either end of this cable is disconnected, USB OTG mode will not be entered. Nothing else is required as the software auto-detects the USB connection and places the DB410c into OTG mode
- Note the when in this mode, devices such as keyboard and mouse may not work on the DB410c

To use USB Host:

- unplug the micro USB cable from J4.  The software will auto-detect no connection on J4 and will enter USB Host mode.
- plug one (or more) USB devices into J2 or J3 such as a keyboard, mouse, etc.

Note that several keyboards, mice, USB Memory sticks, ethernet adaptors
and other peripherals have been tested. However, there is still as
chance that your device might not work properly. If this occurs, please
submit a bug to the 96Boards bug tracking system located
[**here**](https://bugs.96boards.org/enter_bug.cgi?product=Dragonboard%20410c).

If you use an ethernet dongle, once you configure the USB in HOST mode,
and insert the dongle, the physical ethernet connection should automatically be established (generally as eth0, assuming that an active physical connection exists on the ethernet cable):

```shell
ip link show
```
