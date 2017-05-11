# 96Boards HiKey - Work in Progress

**This document is a live document that will be kept updated to reflect changes in the latest snapshot builds for HiKey. Snapshot builds are NOT guaranteed to be stable but reflect the latest work on the HiKey platform. In due course these Getting Started notes will become the Getting Started notes for the next release**

## Getting Started

This document describes how to get started with the HiKey ARMv8 community development boards using latest snapshot software rather than the release build shipped with the boards. 

##### Comments: (information useful for future editing) 
. We need to add this new section: how to upgrade from obsoleted fastboot bootloader to a UEFI bootloader.
This is the same as section: 
Board Recovery
Information on board recovery and/or loading bootloader software on the HiKey board 

THIS IS WORK IN PROGRESS

The current builds this document refers to are:
UEFI snapshot build #91
DEBIAN snapshot build #382

Note that the June 2015 release comes with a fastboot bootloader based on HiSilicon code. It is deprecated and will not be supported in the upcoming releases. This Getting Started Guide therefore refers to the open source ARM Trusted Firmware and UEFI bootloader. It is the supported bootloader for the HiKey board.
 
**The following information is provided in these release notes:**

1. [Snapshot information](#section-0)
2. [Pre-Installed Debian Linux](#section-1)
Information on the Debian 8.0 ("jessie") OS installation software
3. [Installing Android Open Source Project](#section-2)Information on loading the AOSP version of Android 5.1 as an alternative OS onto the HiKey board
4. [Updating the OS](#section-3)
Information on loading an OS update from 96Boards.org
5. [Board Recovery](#section-4)
Information on board recovery and/or loading bootloader software onto the HiKey board
6. [Hardware notes](#section-5)
7. [Known Issues](#section-6)
8. [Building Software from Source Code](#section-7)
Information on building software for the HiKey board from source code
9. [Appendices](#appendix-1)
Information on the partition table used on HiKey and the contents of the boot partition.
10. [New Features](#appendix-2)
Information on key new features introduced in this release. 

### Updating to the new Release

If you already have a HiKey board, you will need to do the following:
- Follow the instructions in [Board Recovery - Installing a Bootloader](#section-41), to update the bootloader on your board
- Follow the instructions in [Updating the OS](#section-3), to install either the Debian or the Android Open Source Project (AOSP) build

<a name="section-0"></a>
## 1. Snapshot release information

- It is recommended to update the booloader and OS together 
- MCU firmware updated <br\>
- PSCI features supported: cpuidle, cpufreq, cpu hotplug and suspend/resume 
- extended support for more HDMI modes 
- switchable through hotkey 'Alt'+'PrtSc'+'g' 
- SYSPLL reads from pctrl registers (therefore compatible with both 1.2GHz and 1.19GHz UEFI) 
- new boot sequence: SD card booting first, fallback to eMMC. 
- SD high speed cards (SDR50, SDR104, and DDR50) are supported.

<a name="section-1"></a>
## 2. Pre-Installed Debian Linux 
The HiKey board is ready to use “out of the box” with a preinstalled version of the Debian Linux distribution.

To get started you will need a power supply, an HDMI monitor, a USB keyboard and a mouse.

**IMPORTANT NOTES**

- HDMI EDID display data is used to determine the best display resolution. On monitors and TVs that support 1080p (or 1200p) this resolution will be selected. If 1080p is not supported the next available resolution reported by EDID will be used. This selected mode will work with **MOST but not all** monitors/TVs. See [below for further information](#section-52) on what to do if your monitor/TV does not display the startup console and UI, and a list of monitors/TVs which can/cannot work with HiKey. 
- There are limitations on the usage of the USB ports on the HiKey board. Please refer to the [Hardware section](#section-53) in the document for further information.

### Power Supply

The HiKey board requires an external power supply providing 12V at 2A. (The board will also work with 9V or 15V power supplies). It is not possible to power the board from a USB power supply because the board can use more power than is available from a standard USB power supply.
 
The HiKey board uses a DC jack with a 1.7mm barrel, center pin positive. An adapter cable is provided with the board to also enable the use of power supplies with 2.1mm barrel jack. 

### Monitor

A standard monitor or TV supporting at least 640x480 resolution is required. Interlaced operation is not currently supported. The maximum resolutions currently supported are 1920x1080p or 1920x1200p. Information on selecting the resolution is [provided below](#section-52).

### Powering up the board

Link 1-2 causes HiKey to auto-power up when power is applied. The other two links should be not fitted (open). If Link 1-2 is not installed then the back edge push button switch is used to power on the HiKey board. 

Please refer to the Hardware User Guide (Chapter 1. Settings Jumper) for more information on board link options.

A few seconds after applying power the right hand green User LED0 will start flashing once per second. The next User LED1 is used as a disk indicator showing access to the on-board eMMC flash memory. The startup console messages will then appear on the connected HDMI screen.

After about 10 seconds the LXDE User Interface will appear and you can start using the HiKey Linux software.

### Wireless Network

The HiKey board includes built in 2.4GHz IEEE802.11g/n WiFi networking. The board does not support the 5GHz band. To use the wireless LAN interface for the first time (or to switch wireless networks) you should click on the wireless LAN icon on the bottom right of the desktop display. The yellow LED between the microUSB and the Type A USB on the front board edge indicates wireless network activity.

You can configure the network from UI, or manually from console:

```
$ sudo nmtui
```

Select 'Activate a connection', Choose your WiFi access point (SSID) and fill the relevant information (password, etc...)

You can check network status by issuing this command.
```
$ sudo nmcli dev wifi
```

### Wired Network

You can connect to a wired network by using a USB Ethernet adapter. Supported adapters should automatically work when the adapter is installed. Please read the [information below](#section-53) on USB port speeds on the HiKey hardware. 

### Bluetooth

The HiKey board includes built-in Bluetooth 4.0 LE support.

To setup a Bluetooth device open the Bluetooth Manager from the Preferences menu. If a “Bluetooth Turned Off” popup appears then select “Enable Bluetooth”. Click on "Search" to search for devices. Try with your bluetooth audio and bluetooth keyboard/mouse. If you make the device trusted then this should operate over a reboot of the board.

The blue LED between the microUSB and the Type A USB on the front board edge indicates bluetooth activity.

### Audio Device

Bluetooth audio devices are supported on HiKey. Follow normal procedures of connecting a bluetooth device to connect to your board.

Note: HDMI audio is not supported in this release.

Once Bluetooth sound sink is connected, you can open the LXMusic player from the Sound & Video menu. Create a playlist from your music files. Supported audio formats are .mp3 and .ogg. You should now be able to play from the LXMusic player. 

### Other Useful Information

**1. Updating and Adding Software**

Before adding any software to your system you must do an update as follows:
```
$ sudo apt-get update
```
You can now add install software to your system:
```
$ sudo apt-get install [package-name]
```
You can search for available packages: 
```
$ apt-cache search [pattern]
```

**2. File Systems**

HiKey comes with two eMMC size: 4GB and 8GB.

**3. Logging in**

The default user name is "linaro" and the default password for user linaro is also "linaro".

**4. Clock**

The HiKey board does not support a battery powered RTC. System time will be obtained from the network if available. If you are not connecting to a network you will need to manually set the date on each power up or use fake-hwclock:
```
$ sudo apt-get install fake-hwclock
```

<a name="section-15"></a>
**5. USB** 

A utility is provided in /home/linaro/bin to change the configuration of the host (Type A and Expansion) and OTG USB ports. By default these ports operate in low/full speed modes (1.5/12 Mbits/s) to support mouse/keyboard devices. Other USB devices such as network or storage dongles/sticks will be limited to full speed mode. Using the usb_speed utility it is possible to support high speed devices (480 Mbits/s) as long as they are not mixed with full/low speed devices.

For information on using the utility do the following:
```
$ sudo usb_speed -h
```
Please refer to the [Hardware Notes section below](#section-53) for further information on the USB port configuration of the HiKey board.

**6. System and User LEDS**

Each board led has a directory in /sys/class/leds. By default the LEDs use the following triggers:

LED | Trigger
--- | -------
wifi_active | phy0tx (WiFi Tx)
bt_active | hci0tx (Bluetooth Tx)
user_led1 | heartbeat
user_led2 | mmc0 (disk access, eMMC)
user_led3 | mmc1 (disk access, microSD card)
user_led4 | CPU core 0 active

To change a user LED you can do the following as a root user:
```
# echo heartbeat > /sys/class/leds/<led_dir>/trigger      make a LED flash
# cat /sys/class/leds/<led_dir>/trigger                   show triggers
# echo none > /sys/class/leds/<led_dir>/trigger           remove triggers    
# echo 1 > /sys/class/leds/<led_dir>/brightness           turn LED on
# echo 0 > /sys/class/leds/<led_dir>/brightness           turn LED off
```

<a name="section-2"></a>
## 3. Installing build of Android Open Source Project 

IMPORTANT NOTE:
Note that for this release you MUST first update the bootloader using the procedure described in [Board Recovery - Installing a Bootloader](#section-41). If you have not already done this then you will need to do so before proceeding.

Users may install a version of the build of Android Open Source Project (AOSP) onto the HiKey board. This will remove the factory installed Debian Linux OS. This section provides instructions on installing the AOSP build which consists of:
- Derived from Linux 3.18 kernel
- AOSP Android Lollipop (5.x)

Download the following files from:
[http://builds.96boards.org/releases/hikey/linaro/aosp/latest](http://builds.96boards.org/releases/hikey/linaro/aosp/latest)
- boot_fat.uefi.img.tar.xz
- cache.img.tar.xz
- system.img.tar.xz
- userdata-4gb.img.tar.xz (for 4G board) or userdata-8gb.img.tar.xz (for 8G board)

Download the following file from:
[http://builds.96boards.org/releases/hikey/linaro/binaries/latest](http://builds.96boards.org/releases/hikey/linaro/binaries/latest)
- [ptable-aosp-4g.img](http://builds.96boards.org/releases/hikey/linaro/binaries/latest/ptable-aosp-4g.img)
or
- [ptable-aosp-8g.img](http://builds.96boards.org/releases/hikey/linaro/binaries/latest/ptable-aosp-8g.img)

Uncompress the .tar.xz files using your operating system file manager, or with the following command for each file:
```
$ tar -Jxf [filename].tar.xz
```
To install updates you will need a Linux PC with fastboot support. For information on installing and setting up UEFI bootloader to HiKey, see [Board Recovery - Installing a Bootloader](#section-41) below.

After setting up fastboot on your Linux PC do the following:

Install Link 5-6 on the HiKey board. This tells the bootloader to start up in fastboot mode.

Power on the HiKey board and verify communications from the Linux PC:
```
$ sudo fastboot devices
0123456789abcdef fastboot
```

Then install the update using the downloaded files.

Note: the ptable must be flashed first. Wait for a few seconds after the reboot command to allow the bootloader to restart using the new partition table.
```
$ sudo fastboot flash ptable ptable-aosp-8g.img
$ sudo fastboot reboot
$ sudo fastboot flash boot boot_fat.uefi.img
$ sudo fastboot flash cache cache.img
$ sudo fastboot flash system system.img
$ sudo fastboot flash userdata userdata.img
```

When flashing is completed power down the HiKey, remove Link 5-6 and power up the HiKey. You may now use the AOSP operating system.  Note the first time boot up will take a couple of minutes. 

Please read the Hardware notes and the Known Issues in this document before using the OS.

<a name="section-3"></a>
## 4. Updating the OS 

Updates to 96Boards supported operating systems will be made available at:
[http://builds.96boards.org/releases/hikey](http://builds.96boards.org/releases/hikey)

Developer snapshot updates are also provided at:
[http://builds.96boards.org/snapshots/hikey](http://builds.96boards.org/snapshots/hikey)

Note that these snapshots represent engineering work in progress towards the next release. They may not be functional or stable and are provided as-is without support. 

IMPORTANT NOTE:
The installation process will overwrite all contents of the eMMC memory. This will remove all installed software and all user files. Before updating the OS, make sure that you have saved any user files or data that you want to keep onto an SD Card or USB memory stick.
Note that for this release you MUST first update the bootloader using the procedure described in [Board Recovery - Installing a Bootloader](#section-41). If you have not already done this then you will need to do so before proceeding.

To install updates you will need a Linux PC with fastboot support. See [section here](#section-42) about how to that for your Linux PC.

Once fastboot is installed on the Linux PC proceed as follows:

### Debian Linux OS

Download the following files onto your Linux PC from: 
[http://builds.96boards.org/releases/hikey/linaro/debian/latest](http://builds.96boards.org/releases/hikey/linaro/debian/latest)
- boot-fat.uefi.img.gz
- hikey-jessie_alip_2015MMDD-nnn-4g.emmc.img.gz (for 4G board) or hikey-jessie_alip_2015MMDD-nnn-8g.img.gz (for 8G board)

Download the following file from:
[http://builds.96boards.org/releases/hikey/linaro/binaries/latest](http://builds.96boards.org/releases/hikey/linaro/binaries/latest)
- [ptable-linux-4g.img](http://builds.96boards.org/releases/hikey/linaro/binaries/latest/ptable-linux-4g.img)
or
- [ptable-linux-8g.img](http://builds.96boards.org/releases/hikey/linaro/binaries/latest/ptable-linux-8g.img)

Note that the Debian image is a large file and may take several minutes (or longer on a slow internet connection) to load. You will need to accept the end user license for the Mali GPU software before you are able to download the OS image. 

Unzip the .gz files (using gunzip or equivalent)

Install Link 5-6 on the HiKey board. This tells the bootloader to start up in fastboot mode. 

Power on the HiKey board and verify communications from the Linux PC:
```
$ sudo fastboot devices
0123456789abcdef fastboot
```

Then install the update using the downloaded files:

Note: the ptable must be flashed first. Wait for a few seconds after the reboot command to allow the bootloader to restart using the new partition table.
```
$ sudo fastboot flash ptable ptable-linux-8g.img
$ sudo fastboot reboot
$ sudo fastboot flash boot boot-fat.uefi.img
$ sudo fastboot flash system hikey-jessie_alip_2015MMDD-nnn-8g.emmc.img
```
When completed, power down the HiKey, remove Link 5-6 and power up the HiKey. If you wish to use a keyboard and mouse in the Type A USB ports, remember to remove the microUSB cable. 

You may now use the updated OS.

**Using an SD Card**

The built-in HiKey eMMC boot software also enables booting a kernel and root file system installed on an SD card. If an SD card is installed at power up the HiKey board will boot the software on the SD Card rather than the software flashed in the eMMC. **TODO: clarify this**

This section describes how to prepare a bootable SD card.

Download the following file onto your Linux PC from: 
[http://builds.96boards.org/releases/hikey/linaro/debian/latest](http://builds.96boards.org/releases/hikey/linaro/debian/latest)
  - hikey-jessie_alip_2015MMDD-nnn-4g.img.gz or hikey-jessie_alip_2015MMDD-nnn-8g.img.gz

Note: _developer version comes with no graphics UI; _alip version comes with LXDE UI.

Extract the file. Install an SD card into your Linux PC. Make sure that you know the SD card device node before carrying out the next step.

**Note:** for this example we assume the device node is `/dev/sdb`. Replace with your assigned SD card device.

```
$ sudo dd if=hikey-jessie_alip_2015MMDD-nnn-8g.img of=/dev/[sdb] bs=4M oflag=sync status=noxfer
```

If your SD card capacity is more than 2GB capacity you may want to change the rootfs to use the rest of the SD card as follows:
```
$ sudo resize-helper
```

### Android Open Source Project (AOSP)

Instructions about installing AOSP on HiKey are described in [this Section above](#section-2).

<a name="section-4"></a>
## 5. Board Recovery 

<a name="section-41"></a>
### Installing a Bootloader 

For most users a board can be “recovered” from a software failure by reloading the operating system using the instructions provided above. However, if the primary bootloader in the eMMC flash memory has been corrupted then the bootloader will need to be re-installed. This section describes how to reinstall the primary bootloader. 
 
**Preparation**

Download the following files onto a Linux PC:
* [l-loader.bin](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/l-loader.bin)
* [fip.bin](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/fip.bin)
* [ptable-linux.img for Debian](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-linux-8g.img) or the 4G variant
* [ptable-aosp.img for AOSP](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-aosp-8.img) or the 4G variant
* [nvme.img](https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/nvme.img)

You will also need the boot partition for the OS Image you want to run
* [boot-fat.uefi.img for Debian] (https://builds.96boards.org/snapshots/hikey/linaro/debian/latest/boot-fat.uefi.img.gz)
* [boot_fat.uefi.img for AOSP](https://builds.96boards.org/snapshots/hikey/linaro/aosp/latest/boot_fat.uefi.img.tar.xz)

You can do this from your browser or from the command prompt:
For example, to download the latest UEFI build and boot partition for Debian do:

```shell
$ wget https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/l-loader.bin
$ wget https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/fip.bin
$ wget https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-linux-4g.img
$ wget https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-linux-8g.img
$ wget https://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/nvme.img
$ wget https://builds.96boards.org/snapshots/hikey/linaro/debian/latest/boot-fat.uefi.img.gz
```

Uncompress the boot image as follows:
```shell
$ gunzip boot-fat.uefi.img.gz
```

You will also need the fastboot application installed on your Linux PC – if this is not installed then follow the instructions [at the end of this section](#section-42). 

Connect a standard microUSB cable between the HiKey microUSB and your Linux PC. Do not power up the HiKey board yet.

**Set Board Link options**

For flashing the bootloader, the top two links should be installed (closed) and the 3rd link should be removed (open):

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | closed
GPIO3-1 | Link 5-6 | open

Link 1-2 causes HiKey to auto-power up when power is installed. Link 3-4 causes the HiKey SoC internal ROM to start up in at a special "install bootloader" mode which will install a supplied bootloader from the microUSB OTG port into RAM, and will present itself to a connected PC as a ttyUSB device.

Please refer to the [Hardware User Guide](https://www.96boards.org/wp-content/uploads/2015/02/HiKey_User_Guide_Rev0.2.pdf) (Chapter 1. Settings Jumper) for more information on the HiKey link options.

Now connect the HiKey power supply to the board.

**Note:** USB does NOT power the HiKey board because the power supply requirements in certain use cases can exceed the power supply available on a USB port. You must use an external power supply.

**Note:** The HiKey board will remain in USB load mode for 90 seconds from power up. If you take longer than 90 seconds to start the install then power cycle the board before trying again.

Wait about 5 seconds and then check that the HiKey board has been recognized by your Linux PC:
```
$ ls /dev/ttyUSB*
or
$ dmesg
```
[hisi-idt.py](https://raw.githubusercontent.com/96boards/burn-boot/master/hisi-idt.py) is the download tool for the HiKey. This is used to install the bootloader as follows:

Execute the following commands as a script or individually:

First, get the script that is needed to load the initial boot software:
```
$ wget https://raw.githubusercontent.com/96boards/burn-boot/master/hisi-idt.py
```
Run the script to initially prepare fastboot (make sure the modem interface is in the right ttyUSB as previously suggested):
```
$ sudo python hisi-idt.py --img1=l-loader.bin
```
After the python command has been issued you should see the following output. If you do not then see the "Problems with Python Downloader" section below
```
+----------------------+
 Serial:  /dev/serial/by-id/usb-䕇䕎䥎_㄰㌲㔴㜶㤸-if00-port0
 Image1:  l-loader.bin
 Image2:  
+----------------------+

Sending l-loader.bin ...
Done
```
Note: You may see the word “failed” before Done. This is under investigation but is not fatal. As long as the “Done” is printed at the end you may proceed.

The bootloader has now been installed into RAM. Wait a few seconds for the fastboot application to actually load. The following fastboot commands then load the partition table, the bootloader and other necessary files into the HiKey eMMC flash memory (4G or 8G).
```
$ sudo fastboot flash ptable ptable-linux-8g.img
$ sudo fastboot flash fastboot fip.bin
$ sudo fastboot flash nvme nvme.img
$ sudo fastboot flash boot boot-fat.uefi.img
$ sudo fastboot reboot
```

Once this has been completed the bootloader has been installed into eMMC.
Power off the HiKey board by removing the power supply jack.

Next change the link configuration as follows:

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | open
GPIO3-1 | Link 5-6 | closed

Now connect power supply jack to your HiKey again.

Check that the HiKey board is detected by your Linux PC

You should see the ID of the HiKey board returned
```
$ sudo fastboot devices
0123456789abcdef fastboot
```

Your bootloader has been successfully installed and you are now ready to install the operating system system files into the eMMC flash memory (see [Updating the OS](#section-3), above). For Debian you will need to load only the system partition, and for AOSP the cache, system and user data partitions. 

**Note:**

This bootloader is based on UEFI and includes:
- ARM Trusted Firmware
- UEFI with DeviceTree
- GRUB
- fastboot support
- OP-TEE support

For further information on the bootloader, including how to build it from source, see the 96Boards documentation here:
- [HiKey Bootloader Wiki](https://github.com/96boards/documentation/wiki/HiKeyUEFI)

<a name="section-42"></a>
**Installing Fastboot onto your PC**
Step 1: Use the following commands
```
$ sudo apt-get install android-tools-fastboot      On Debian/Ubuntu
$ sudo yum install android-tools                   On Fedora
```

Step 2: Either create the file: /etc/udev/rules.d/51-android.rules with the following content, or append the content to the file if it already exists. You will need to have superuser privileges so use
```
$ sudo vim /etc/udev/rules.d/51-android.rules       or 
$ sudo gedit /etc/udev/rules.d/51-android.rules
```
 to create and edit the file.  Add the following to the file.

```
# fastboot protocol on HiKey
SUBSYSTEM=="usb", ATTRS{idVendor}=="18d1", ATTRS{idProduct}=="d00d", MODE="0666"
# adb/rndis protocol on HiKey
SUBSYSTEM=="usb", ATTRS{idVendor}=="12d1", MODE="0666", ENV{ID_MM_DEVICE_IGNORE}="1"
```

**Problems with Python Downloader**<br>

The Python download script requires Python 2. Make sure you're not defaulted to Python 3 by typing:
```
$ python --version
```

**Note:** Python 3 currently has a serial library bug, and will fail during data transfer - so if you are using Python 3 then you will need to install and/or change to Python 2.7:
```
$ sudo apt-get install python2.7 python2.7-dev
$ alias python=python2.7
```

If you get the following error message while running the hisi-idt.py script:
```
ImportError: No module named serial
```
Then you need to install the python-serial module, on Ubuntu/Debian run:
```
$ sudo apt-get install python-serial
```
or you can use pip install:
```
$ sudo pip install pyserial
```
If you have Python 3 installed, make sure to install with the right version, for instance:
```
$ sudo pip2.7 install pyserial
```

<a name="section-5"></a>
## 6. Hardware Notes 

### Schematics and HiKey Board Hardware User Guide
- [Schematics](https://www.96boards.org/hikey-schematics)
- [Hardware User Guide](https://www.96boards.org/hikey-userguide)

<a name="section-51"></a>
### CPU Load
 
The current supplied Linux 3.18-based kernel supports the thermal protection framework and DVFS. This will cause the HiKey CPU core frequencies to be reduced from the maximum 1.2GHz if the thermal setpoint of the SoC is reached. In an extreme case thermal shutoff will occur if DVFS has not been effective at reducing the SoC temperature to an acceptable level.

Higher performance may be obtained by using forced air (fan) cooling on the HiKey board.

<a name="section-52"></a>
### HDMI Port
The HDMI port on HiKey supports TVs and Monitors with an HDMI interface at display resolutions of 1920x1080 (or 1920x1200) pixels down to 640x480 pixels. Note that interlaced display modes are not currently supported. **The mobile SoC used on the HiKey board does not have sufficiently flexible clocking arrangements to support all possible HDMI timings specified by monitors.** A list of monitors/TVs can/cannot work with HiKey is stored [here](https://docs.google.com/spreadsheets/d/1aYNUlGx5J8RmfWrtML4lFJ-7tV8u-3AH_3IaiOlWfoE/edit?usp=sharing).

Note also that these modes are preferred and will be tried (in this order) first if they exist in EDID:

Resolution | Ratio | Usually on
---------- | ----- | ----------
1920x1200 | 16:10 | Monitor
1920x1080 | 16:9 | TV
1680x1050 | 16:10 | Monitor
1280x1024 | 5:4 | Monitor
1280x720 | 16:9 | TV
800x600	| 4:3 | TV

**Debian Build**

The Debian build uses Linux DRM drivers for the graphics system. When first powered up the board will attempt to read the EDID display data from the attached TV/monitor. It will then select the highest resolution format available up to 1920x1080 pixels. If all is well the selected default will work on your TV/Monitor and no more needs to be done. However, in some cases your monitor may not display correctly on the selected setting because the timing is not exactly correct. In this case we have provided a facility to enable you to cycle through the available EDID modes to find one that works correctly for your TV/Monitor.

After the board has booted the User0 LED should be heartbeating about once per second. Using a connected keyboard cycle through the available EDID modes using the following key sequence: Right_Alt + PrintScr + G

Press the right hand Alt Key then the PrintScr key and then the G key so that finally all the keys are pressed. Each time you enter this sequence the HiKey will switch to the next screen mode. When you find one that works well write down the resolution and frequency from the TV display - for example 1280x1024@75Hz.

Once you have found a working resolution you may edit the grub.cfg file to make the new mode the default boot mode. Be very careful not to make mistakes editing this file or your kernel may not boot correctly. Note this file needs su privileges to modify:
```
$ sudo vim /boot/grub/grub.cfg
```
Add the chosen display format into the file at the end of the linux line in the following format, replacing 1280x1024 with your selected resolution and 75 with your selected frequency.
```
video=HDMI-A-1:1280x1024@75
```
[Note - at present the desktop screen does not resize correctly for each resolution - this is planned to be fixed in a future release.]

Finally, **if you are still having difficulties you can revert to a built-in 720p mode by starting up the HiKey with no HDMI device attached**. The UI will then come up at a fixed 1280x720 resolution which will be used when you then plug the HDMI monitor in. **Pressing hotkey Alt+PrtScr+g  will switch to a built-in 800x600 mode, then cycle back.**

**Android Build**

[Information on Android To Be Written]
At the moment resolution is locked at **1280x720p.**(do not use hotkey Alt+PrtScr+g).<br\>

Note: the fixed settings may not work on all monitors/TVs but have been demonstrated to work on most. 

<a name="section-53"></a>
### USB Ports 

There are multiple USB ports on the HiKey board:
- One microUSB OTG port on the front edge of the board
- Two Type A USB 2.0 host ports on the front edge of the board
- One USB 2.0 host port on the high-speed expansion bus

Please read the HiKey Board Hardware User Guide for more information on the following hardware restrictions:

1. The microUSB OTG port may be used (in host or slave mode) OR the Type A host ports may be used. They may not both be used simultaneously. If a cable is inserted into the OTG port then the Type A ports and the expansion bus port will be automatically disabled.
2. For the microUSB OTG port a single Low Speed (1.5Mbit/sec), Full Speed (12Mbit/sec) or High Speed (480Mbit/sec) device is supported.
3. For the USB host ports all attached USB devices MUST be one of the following two options:
    - Low Speed (1.5Mbit/sec) and Full Speed (12Mbit/sec) devices, or
    - High Speed devices (480Mbit/sec)

If a mixture of High Speed and Low/Full speed devices are attached the devices will not operate correctly. This also applies if any hubs are attached to the ports.

The reason for this limitation is that USB 2.0 split transfers are not supported by the mobile-targeted SoC hardware USB implementation.

In order to address this limitation the USB ports are by default configured into Low/Full speed operation.

In Debian the `usb_speed utility` (use `-h` option for help) is provided to switch the USB ports between modes (see [Other Useful Information](#section-15) above for details on this utility).

In the AOSP build a small application is provided (usb-speed-switch) to change between High Speed and Full Speed operation.

<a name="section-54"></a>
### UART Ports 

In Debian, the two 96Boards expansion IO UART serial ports will appear as `/dev/ttyAMA2` and `/dev/ttyAMA3` and are configured at 115200 baud by default. Note: UEFI and kernel console are directed to /dev/ttyAMA3.

Alternatively, you can modify grub.cfg in the system image to switch to other UART.

* /boot/grub/grub.cfg (linux boot line) <br\>
`linux /boot/Image console=tty0 console=ttyAMA3,115200 root=/dev/disk/by-partlabel/system rootwait rw quiet efi=noruntime`

Note: the LS expansion port I/O pins on the 96Boards 2mm header, including the UART signals, are at **1.8V** levels. 

<a name="section-6"></a>
## 7. Known Issues 

The following are known issues on the current release.

1. **Not Yet Supported**
    - HDMI and Expansion bus audio. At present only Bluetooth audio is supported (on both Debian and AOSP builds)
    - Video playback in Debian. This will be addressed in a future software release **TODO: check this**
    - Some video formats are not decoded in Android, and will not be played with the current release
    - HDMI hotplug detection (HPD) is not supported. (Note: You can still plug in HDMI cable after the board booted, which make HDMI work through two candidate modes: 1280x720p and 800x600p. However these are not HPD, they is built-in default.)
    - Hardware graphics acceleration (Mali GPU) for OpenGL ES on the Debian build. This will be addressed in a future software release. GPU acceleration is functional in the AOSP build **TODO: check this**
    - The Bluetooth LED is not functional in the Android build **TODO: check this**
    - Power button behavior. **TODO: check this**
2. **USB gives occasional non-fatal kernel trace messages**
`usb usb1: clear tt 1 (9032) error -22`
This is under current investigation.
3. **Apple Bluetooth Keyboards/Mice/Trackpads do not work**
This is under current investigation. 
4. **TODO: any known bug, need to be added here **

**Reporting New Issues**

To report new issues with the HiKey please use the [96Boards Bug Tracker](https://bugs.96boards.org).

To view the open bugs, click [here](https://bugs.96boards.org/buglist.cgi?bug_status=__open__&list_id=144&order=Importance&product=HiKey&query_format=specific).  Alternatively go to the [site](https://bugs.96boards.org/), click on the Search menu item, select HiKey in the product dropdown list and click the search button.

To add a new bug, click [here](https://bugs.96boards.org/enter_bug.cgi?product=HiKey) and fill in the form.  Alternatively go to the [site](https://bugs.96boards.org/), click on the New menu item, then click on Consumer Edition Boards, then click on HiKey, and finally fill in the form.

<a name="section-7"></a>
## 8. Building Software from Source Code 

THIS SECTION NEEDS REVIEW AND UPDATE FOR NEW RELEASE
DO NOT USE THESE INSTRUCTIONS 
UPDATE FOR NEW KERNEL, WIFI MODULE AND BOOTLOADER

### Bootloader From Source

For further information on the bootloader building from source, see the 96Boards documentation here:
- [HiKey Bootloader Wiki](https://github.com/96boards/documentation/wiki/HiKeyUEFI)

### Kernel From Source

To build a kernel using a linux computer use the following instructions. These assume that you have a good level of knowledge in using Linaro tools and building Linux kernels.

The HiKey kernel sources are located at: [https://github.com/96boards/linux](https://github.com/96boards/linux)

To build a kernel, make sure you have an AArch64 cross-toolchain installed on your linux computer, and configured to cross compile to ARMv8 code. For example, Linaro GCC 4.9:
```
$ wget http://releases.linaro.org/14.09/components/toolchain/binaries/\ 
> gcc-linaro-aarch64-linux-gnu-4.9-2014.09_linux.tar.xz
$ mkdir ~/arm64-tc/bin
$ tar --strip-components=1 -C ~/arm64-tc/bin -xf gcc-linaro-aarch64-linux-gnu-4.9-2014.09_linux.tar.xz
$ export PATH=~/arm64-tc/bin:$PATH
```

Note: the toolchain binaries are for a 32 bit host system. On Debian/Ubuntu, you should install multiarch-support and enabled i386 architecture. On Fedora, you should install glibc.i686 package.

The following instructions can then be used to build the kernel:

Git clone the source code tree:
```
$ git clone https://github.com/96boards/linux.git
$ git checkout -b working-hikey 96boards-hikey-15.05 **TODO:update this**
```

To build the kernel:
```
$ export LOCALVERSION="-linaro-hikey"

$ make distclean 
$ make ARCH=arm64 CROSS_COMPILE=aarch64-linux-gnu- defconfig 
$ make ARCH=arm64 CROSS_COMPILE=aarch64-linux-gnu- -j8 Image modules hi6220-hikey.dtb
```

### WiFi Driver From Source

The rootfs included in each hikey release uses a different wifi driver than the one defined in the kernel.config file present in the release page.
https://builds.96boards.org/snapshots/hikey/linaro/debian/latest

By default, hikey includes the TI R8.5 wl18 driver (some information below)
http://processors.wiki.ti.com/index.php/WL18xx_System_Build_Scripts

In order to compile and install this driver you will have to do the following:

```
$ git clone https://github.com/96boards/linux linux.git
$ cd linux.git
$ git clone https://github.com/96boards/wilink8-wlan_build-utilites.git build_utilities.git
$ git clone -b hikey https://github.com/96boards/wilink8-wlan_wl18xx.git build_utilities.git/src/driver
$ git clone -b R8.5  https://github.com/96boards/wilink8-wlan_wl18xx_fw.git build_utilities.git/src/fw_download
$ git clone -b hikey https://github.com/96boards/wilink8-wlan_backports.git build_utilities.git/src/backports
$ patch -p1 < build_utilities.git/patches/hikey_patches/0001-defconfig-hikey-discard-CFG80211-and-MAC80211.patch
```

Then compile the kernel as usual. 
Before building the kernel drivers, create a file build_utilities.git/setup-env using the build_utilities.git/setup-env.sample as reference.

Please ignore any warnings/errors reported during the following steps

```
$ cd linux.git
$ make ARCH=arm64 CROSS_COMPILE=aarch64-linux-gnu- -j8 modules INSTALL_MOD_PATH=./build_utilities.git/fs\
> modules_install
$ cd build_utilities.git
$ ./build_wl18xx.sh modules
$ ./build_wl18xx.sh firmware
```

Now, all the kernel drivers and kernel firmwares have been installed in build_utilities.git/fs/lib.

Make sure to remove the following file: lib/firmware/ti-connectivity/wl18xx-conf.bin to avoid the possibility of a format mismatch (the new firmware wl18xx-fw-4.bin uses its internal conf data)

You could now chown root:root the directory, compress it and decompress it in your final target (or an intermediate rootfs if you are generating an image)

```
$ cd build_utilities.git/fs/lib
$ rm firmware/ti-connectivity/wl18xx-conf.bin
$ sudo chown -R root:root *
$ sudo tar jcvf fw-modules.tar.bz2 * 
```

To include the drivers compiled above in a jessie image you would:

a) install simg2img and make_ext4fs both from Linaro's modified package 'android-tools-fsutils' 

```
$ wget http://repo.linaro.org/ubuntu/linaro-overlay/pool/main/a/android-tools/\
> android-tools-fsutils_4.2.2+git20130218-3ubuntu41+linaro1_amd64.deb
$ sudo dpkg -i --force-all android-tools-fsutils_*.deb
```

b) then do the following 

```
$ gzip -d -c hikey-jessie_developer_2015MMDD-nnn.img.gz > /tmp/jessie.img
$ simg2img /tmp/jessie.img /tmp/raw.img
$ mkdir /tmp/mnt
$ sudo mount /tmp/raw.img /tmp/mnt
$ cd /tmp/mnt/lib/
$ sudo tar xvf fw-modules.tar.bz2
$ cd /tmp/
$ sudo make_ext4fs -o -L rootfs -l 2G -s jessie.updated.img mnt/ 
$ sudo umount mnt/
```

Note: by rebuilding the image file you could also transfer your public ssh keys or private files - like wifi credentials - to the target before booting it.

At this point you would have an image with the required drivers. Now You will need to decide whether you want your kernel to install on internal eMMC, or on an installed microSD card.

### Install onto eMMC

To build the boot image for eMMC:

**Method 1 - Build from scratch**

Create a dummy ramdisk for the ramdisk image:
```
$ touch initrd ; echo initrd | cpio -ov > initrd.img
```

Create the boot image (KERN=Image, DTB=hi6220-hikey.dtb, RAMDISK=initrd.img)
```
$  mkdir boot-fat
$  dd if=/dev/zero of=boot-fat.uefi.img bs=512 count=131072
$  sudo mkfs.fat -F32 -n "boot" out/boot-fat.uefi.img
$  sudo mount -o loop,rw,sync boot-fat.uefi.img boot-fat
$  sudo cp -a $KERN $DTB $RAMDISK boot-fat/ || true
$  sudo mkdir -p boot-fat/EFI/BOOT
$  sudo cp -a AndroidFastbootApp.efi boot-fat/EFI/BOOT/fastboot.efi || true
$  sudo cp -a grubaa64.efi boot-fat/EFI/BOOT/grubaa64.efi || true
$  sudo cp -a grub.cfg boot-fat/EFI/BOOT/grub.cfg || true
$  sudo umount boot-fat
$  sudo rm -rf boot-fat
```

After the above, you can flash the boot-fat.uefi.img to eMMC with the command:
```
$ sudo fastboot flash boot boot-fat.uefi.img
$ sudo fastboot reboot
```

**Method 2 - Use an existing boot-fat.uefi.img**
```
$ mkdir tmp
$ sudo mount boot-fat.uefi.img tmp
$ sudo cp YOUR-KERNEL-BUILD/arch/arm64/boot/Image tmp/Image
$ sudo cp YOUR-KERNEL-BUILD/arch/arm64/boot/dts/hi6220-hikey.dtb tmp/
$ sudo umount tmp
$ rm -rf tmp
```

After the above, you can flash the boot-fat.uefi.img to eMMC with the command:
```
$ sudo fastboot flash boot boot-fat.uefi.img
$ sudo fastboot reboot
```

Note: if you make ANY of your own changes to the tagged tree your built kernel will be named 3.18.0-linaro-hikey+ (use `uname -a` to see the kernel name). This means that the installed kernel modules in /lib/modules will not work correctly unless you install a new set of kernel modules in /lib/modules from your kernel build.

### Install onto SD card
**TODO: change this. UEFI always load kernel from eMMC's boot partition**

1. Use the kernel Image and hi6220-hikey.dtb as explained above.
2. Prepare your SD card. See [Using an SD Card]() for more information. There will be two partitions on it: `boot` and `rootfs`
3. Insert your SD card into your Linux PC and copy your newly built kernel and device tree blob onto the SD card boot partition - use your own SD card /dev device in place of /dev/sda1:
```
$ sudo cp -a arch/arm64/boot/Image arch/arm64/boot/dts/hi6220-hikey.dtb /dev/sda1/boot/
```

Note: File names must not be changed - Refer to [Appendix 1](#appendix-1) to see the 4 files that are expected to be in the boot partition. If any of these are missing from the SD card boot partition, HiKey will fall back to the eMMC boot partition and boot from eMMC.

Plug your SD card to HiKey board.

**Source for jessie rootfs build**

We pull most of the packages from Debian official repository. The remaining changes are available in github at [https://github.com/96boards](https://github.com/96boards)

### AOSP Build From Source

AOSP sources are hosted in these repositories:
- [https://github.com/96boards/android_hardware_ti_wpan](https://github.com/96boards/android_hardware_ti_wpan)
- [https://github.com/96boards/android_external_wpa_supplicant_8](https://github.com/96boards/android_external_wpa_supplicant_8)
- [https://github.com/96boards/android_device_linaro_hikey](https://github.com/96boards/android_device_linaro_hikey)
- [https://github.com/96boards/android_manifest](https://github.com/96boards/android_manifest)

**Build setup**

Please setup the host machine by following the instructions here: [http://source.android.com/source/initializing.html](http://source.android.com/source/initializing.html)

NOTE: The build tries to mount a loop device as fat partition to create the boot-fat.img filesystem image. Please make sure your user is allowed to run those commands in sudo without password by running "visudo" and appending the following lines (replacing "`<USER>`" with your username):
```
<USER> ALL= NOPASSWD: /bin/mount
<USER> ALL= NOPASSWD: /bin/umount
<USER> ALL= NOPASSWD: /sbin/mkfs.fat
<USER> ALL= NOPASSWD: /bin/cp
```

Here are the instructions on how to download the code:
```
$ mkdir android/
$ cd android/
```

Download and extract the Mali vendor binaries in the above directory:
http://builds.96boards.org/snapshots/hikey/linaro/binaries/20150607/vendor.tar.bz2
```
$ repo init -u https://android.googlesource.com/platform/manifest -b android-5.1.1_r1\
> -g "default,-device,hikey"
$ cd .repo/
$ git clone https://github.com/96boards/android_manifest -b android-5.0 local_manifests
$ cd -
$ repo sync -j8
$ source build/envsetup.sh
$ lunch hikey-userdebug
$ make droidcore -j8
$ cd out/target/product/hikey
```

Install the built files following the instructions on loading the AOSP build in [Section 2](#section-2) above. 

<a name="appendix-1"></a>
### Appendix 1: Partition Information 

<a name="table-1"></a>
Table 1 describes the partition layout on the HiKey eMMC.
**TODO: need an update for 4GB and 8GB eMMC**

Name | Partition | Offset | Size
---- | --------- | ------ | ----
fastboot1 | -- | 0x0000_0000 | 0x0004_0000 (256KB)
ptable | -- | 0x0000_0000 | 0x0010_0000 (1MB)
vrl | 1 | 0x0010_0000 | 0x0010_0000 (1MB)
vrl_backup | 2 | 0x0020_0000 | 0x0010_0000 (1MB)
mcuimage | 3 | 0x0030_0000 | 0x0010_0000 (1MB)
fastboot | 4 | 0x0040_0000 | 0x0080_0000 (8MB)
nvme | 5 | 0x00C0_0000 | 0x0020_0000 (2MB)
boot | 6 | 0x00E0_0000 | 0x0400_0000 (64MB)
Reserved |7 | 0x04E0_0000 | 0x1000_0000 (256MB)
cache | 8 | 0x14E0_0000 | 0x1000_0000 (256MB)
system | 9 | 0x24E0_0000 | 0x6000_0000 (1536MB)
userdata | 10 | 0x84E0_0000 | 0x6000_0000 (1536MB)

Table 1: HiKey Partitions

Note that for the Debian build the system and userdata partitions are merged to create a single system (root file system) partition. 

<a name="table-2"></a>
Table 2 describes the binaries located in the boot partition.

File Name | Description | Supported Max. Size
--------- | ----------- | -------------------
Image | Kernel Image<sup>1</sup> | 16MB
initrd.img | Ramdisk Image | 8MB
hi6220-hikey.dtb | Device Tree Binary<sup>2</sup> |512KB
fastboot.efi | fastboot application for UEFI | N/A
grubaa64.efi | grub application for Aarch64 for UEFI | N/A
grub/grub.conf | grub configuration file | N/A

Table 2: boot partition files

Note<sup>1</sup>: Kernel build image: `arch/arm64/boot/image`
Note<sup>2</sup>: DTB: `arch/arm64/boot/dts/hi6220-hikey.dtb`

<a name="appendix-2"></a>
### Appendix 2: New Features 
N/A