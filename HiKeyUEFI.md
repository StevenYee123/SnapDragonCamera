# HiKey - UEFI

##Table of Contents

  * [HiKey - UEFI](#hikey---uefi)
    * [Install from prebuilt binaries](#install-from-prebuilt-binaries)
    * [Source code](#source-code)
    * [Build instructions](#build-instructions)
      * [Install custom toolchain(s)](#install-custom-toolchains)
      * [Get the source code](#get-the-source-code)
      * [Build UEFI for HiKey](#build-uefi-for-hikey)
      * [Create or modify menu entry in GRUB](#create-or-modify-menu-entry-in-grub)
      * [EFI boot partition (boot-fat.uefi.img)](#efi-boot-partition-boot-fatuefiimg)
    * [Flash binaries to eMMC <a name="user-content-flash-emmc"></a> ](#flash-binaries-to-emmc-)
    * [Run Fastboot from UEFI](#run-fastboot-from-uefi)
      * [Boot Sequence](#boot-sequence)
    * [Set Serial Number in recovery mode](#set-serial-number-in-recovery-mode)
    * [Control user led](#control-user-led)
    * [How to Use UART0 as console](#how-to-use-uart0-as-console)
    * [Known Issues](#known-issues)


**NOTE**: Throughout this document, when you see Jumper pin1-6 on **J15**, it refers to original HiKey boards built by CircuitCo<sup>R</sup>. If the board you have is built by **LEMAKER<sup>R</sup>**, then read these as "Jumper pin1-6 on **J601**"

The following binaries are required:
* l-loader.bin - used to switch from aarch32 to aarch64 and boot
* fip.bin - firmware package
* ptable-aosp-[4g|8g].img or ptable-linux-[4g|8g].img - partition tables for respectively AOSP images or Linux images
* kernel and dtb images - included in the boot partition

## Install from prebuilt binaries

Latest UEFI snapshot builds are published [here](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest). Download the following files:
* [l-loader.bin](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/l-loader.bin)
* [fip.bin](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/fip.bin)
* [ptable-linux-4g.img](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-linux-4g.img) or [ptable-linux-8g.img](http://builds.96boards.org/snapshots/hikey/linaro/uefi/latest/ptable-linux-8g.img)

Note: Latest UEFI release build is published [here](http://builds.96boards.org/releases/hikey/linaro/binaries/latest/), and you can go to this link to get the prebuilt binaries.

Latest Debian snapshot builds are published [here](https://builds.96boards.org/snapshots/hikey/linaro/debian/latest). You can pick a boot partition and an eMMC rootfs:
* boot-fat.uefi.img.gz
* hikey-jessie_developer_YYYYMMDD-XXX-4g.emmc.img.gz or hikey-jessie_developer_YYYYMMDD-XXX-8g.emmc.img.gz

Latest Debian release build is published [here](https://builds.96boards.org/releases/hikey/linaro/debian/latest).

For example, to download the latest UEFI build and Debian build:

```shell
wget https://builds.96boards.org/releases/hikey/linaro/uefi/latest/l-loader.bin
wget https://builds.96boards.org/releases/hikey/linaro/uefi/latest/fip.bin
wget https://builds.96boards.org/releases/hikey/linaro/uefi/latest/ptable-linux.img
wget https://builds.96boards.org/releases/hikey/linaro/debian/latest/boot-fat.uefi.img.gz
wget https://builds.96boards.org/releases/hikey/linaro/debian/latest/hikey-jessie_developer_YYYYMMDD-XXX-4g.emmc.img.gz (or hikey-jessie_developer_YYYYMMDD-XXX-8g.emmc.img.gz)
gunzip *.img.gz
```

Now skip to the [Flash binaries to eMMC](#flash-emmc) section.

## Source code

The source code is available from:
* [l-loader](https://github.com/96boards/l-loader)
* [ARM Trusted Firmware](https://github.com/96boards-hikey/arm-trusted-firmware)
* [EDK II (UEFI)](https://github.com/96boards/edk2)
* (Optional) [OP-TEE] (https://github.com/OP-TEE/optee_os)

## Build instructions

Prerequisites:
* GCC 4.8 or 4.9 - cross-toolchain for Aarch64 available in your PATH. [Linaro GCC 4.9-2015.02](http://releases.linaro.org/archive/15.02/components/toolchain/binaries/aarch64-linux-gnu/gcc-linaro-4.9-2015.02-3-x86_64_aarch64-linux-gnu.tar.xz) is used in the build instructions.
* GCC cross-toolchain for gnueabihf available in your PATH. [Linaro GCC 4.9-2015.02](http://releases.linaro.org/archive/15.02/components/toolchain/binaries/arm-linux-gnueabihf/gcc-linaro-4.9-2015.02-3-x86_64_arm-linux-gnueabihf.tar.xz) is used in the build instructions.
* GPT fdisk (gdisk package from your favorite distribution).

### Install custom toolchain(s)

```shell
mkdir arm-tc arm64-tc
tar --strip-components=1 -C ${PWD}/arm-tc -xf gcc-linaro-4.9-*-x86_64_aarch64-linux-gnu.tar.xz
tar --strip-components=1 -C ${PWD}/arm64-tc -xf gcc-linaro-4.9-*-x86_64_arm-linux-gnueabihf.tar.xz
export PATH="${PWD}/arm-tc/bin:${PWD}/arm64-tc/bin:$PATH"
```

### Get the source code

```shell
# --depth 1 means to fetch code without commit history
git clone -b hikey-aosp --depth 1 https://github.com/96boards-hikey/edk2.git linaro-edk2
git clone -b hikey --depth 1 https://github.com/96boards-hikey/arm-trusted-firmware.git
git clone -b hikey-aosp --depth 1 https://github.com/96boards-hikey/LinaroPkg.git
git clone --depth 1 https://github.com/96boards-hikey/l-loader.git
git clone git://git.linaro.org/uefi/uefi-tools.git

# Optionally, if also building OP-TEE
git clone --depth 1 https://github.com/OP-TEE/optee_os.git
```

### Build UEFI for HiKey

```shell
export AARCH64_TOOLCHAIN=GCC49
export EDK2_DIR=${PWD}/linaro-edk2
export UEFI_TOOLS_DIR=${PWD}/uefi-tools

cd ${EDK2_DIR}
${UEFI_TOOLS_DIR}/uefi-build.sh -c ../LinaroPkg/platforms.config -b RELEASE -a ../arm-trusted-firmware hikey

# NOTE: If also building OP-TEE, run below command instead of the one above
# ${UEFI_TOOLS_DIR}/uefi-build.sh -c ../LinaroPkg/platforms.config -b RELEASE -a ../arm-trusted-firmware -s ../optee_os hikey

# To use UART0 instead of UART3 as the console, uncomment the appropriate line(s) in
# ../LinaroPkg/platforms.config before running the command above.

cd ../l-loader
ln -s ${EDK2_DIR}/Build/HiKey/RELEASE_GCC49/FV/bl1.bin
ln -s ${EDK2_DIR}/Build/HiKey/RELEASE_GCC49/FV/fip.bin
arm-linux-gnueabihf-gcc -c -o start.o start.S
arm-linux-gnueabihf-gcc -c -o debug.o debug.S
arm-linux-gnueabihf-ld -Bstatic -Tl-loader.lds -Ttext 0xf9800800 start.o debug.o -o loader
arm-linux-gnueabihf-objcopy -O binary loader temp
python gen_loader.py -o l-loader.bin --img_loader=temp --img_bl1=bl1.bin
# XXX sgdisk usage requires sudo
sudo PTABLE=linux-4g bash -x generate_ptable.sh
python gen_loader.py -o ptable-linux.img --img_prm_ptable=prm_ptable.img
```

The files fip.bin, l-loader.bin and ptable-linux.img are now built. All the image files are in $BUILD/l-loader directory. The Fastboot App is at $EDK2_DIR/Build/HiKey/RELEASE_GCC49/AARCH64/AndroidFastbootApp.efi

### Create or modify menu entry in GRUB
GRUB is the loader to OS. Now both debian and AOSP are booted by GRUB on HiKey. If user wants to create his own kernel, creating a new menu entry is preferred.

grub.cfg is stored in /EFI/BOOT directory of boot partition (boot-fat.uefi.img). Append new entry in grub.cfg. In this case, only custom kernel and dtb file are stored in rootfs. And still use the initrd that are installed in rootfs.

```shell
menuentry 'Custom Kernel' {
    search.fs_label rootfs root
    search.fs_label boot esp
    linux ($esp)/Image console=tty0 console=ttyAMA3,115200 root=/dev/disk/by-partlabel/system rootwait rw efi=noruntime
    initrd ($root)/boot/initrd.img
    devicetree ($esp)/hi6220-hikey.dtb
}
```

User can also store custom kernel & dtb file into rootfs. Just update grub.cfg according to the change.

Since boot partition could be mounted after system boot, user could edit grub.cfg directly. Or user could unpack boot-fat.uefi.img, edit it and pack again.

### EFI boot partition (boot-fat.uefi.img)
The boot partition is a 64MB FAT partition. In prebuilt boot partition, there are grubaa64.efi, grub.cfg and fastboot.efi. Kernel, initrd and dtb files are stored in rootfs (partition 9 in eMMC).

If user wants to create his own boot partition, it's better to be based on prebuilt boot-fat.uefi.img. See Getting Started instructions for more information on building the kernel. See GRUB section for more information on creating new entry in grub.cfg.
```shell
$ mkdir -p boot-fat
$ sudo mount -o loop,rw,sync boot-fat.uefi.img boot-fat
# command to copy custom kernel & dtb file into boot-fat
$ sync
$ sudo umount boot-fat.uefi.img
```

## Flash binaries to eMMC <a name="flash-emmc"></a>

The flashing process requires to be in **recovery mode** if user wants to update l-loader.bin.

* turn off HiKey board
* connect debug UART on HiKey to PC (used to monitor debug status)
* make sure pin1-pin2 and pin3-pin4 on J15 are linked (recovery mode)

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | closed
GPIO3-1 | Link 5-6 | open

* connect HiKey Micro-USB to PC with USB cable
* turn on HiKey board
* on serial console, you should see some debug message (NULL packet)
* run [HiKey recovery tool](https://raw.githubusercontent.com/96boards/burn-boot/master/hisi-idt.py) to flash l-loader.bin (Note: if the serial port recorded in hisi-idt.py isn't available, adjust the command line below by manually setting the serial port with "-d /dev/ttyUSBx" where x is usually the last serial port reported by "dmesg" command)
```shell
$ sudo python hisi-idt.py --img1=l-loader.bin
```

**do not reboot yet**
* run fastboot commands to flash the images (**order must be respected**)
```shell
$ wget https://builds.96boards.org/releases/hikey/linaro/binaries/latest/nvme.img
$ sudo fastboot flash ptable ptable-linux.img
$ sudo fastboot flash fastboot fip.bin
$ sudo fastboot flash nvme nvme.img
$ sudo fastboot flash boot boot-fat.uefi.img
$ sudo fastboot flash system hikey-jessie_alip_2015MMDD-nnn.emmc.img
```
* turn off HiKey board
* remove the jumper of pin3-pin4 on J15

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | open
GPIO3-1 | Link 5-6 | open

* turn on HiKey board

Note: UEFI may report "Fail to start BootNext" if you have an SD card installed, see [Boot Sequence](#boot-sequence) for more information. 

## Run Fastboot from UEFI
* make sure that jumper of pin5-pin6 on J15 are connected

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | open
GPIO3-1 | Link 5-6 | closed

* Or Interrupt the boot by pressing any select
```shell
The default boot selection will start in 10 seconds
[1] fastboot
[2] boot from eMMC
[3] boot from SD card
[4] Shell
[5] Boot Manager
Start:
```
* At the "Shell>" prompt, type: fastboot
```shell
Shell> fastboot
Android Fastboot mode - version 0.4. Press any key to quit
```
* fastboot configuration in host  
```shell
sudo apt-get install android-tools-fastboot
```
* host use:  
```shell
$ sudo fastboot flash ptable ptable-linux.img
$ sudo fastboot flash fastboot fip.bin  
$ sudo fastboot flash nvme nvme.img  
$ sudo fastboot flash boot boot-fat.uefi.img  
$ sudo fastboot flash system system.img  
$ sudo fastboot flash cache cache.img  
$ sudo fastboot flash userdata userdata.img  
# l-loader.bin should be flashed in recovery mode only

```
* Run Fastboot automatically:

Connect jumper on pin5-6 of J15. Then UEFI will run Fastboot app directly.

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | open
GPIO3-1 | Link 5-6 | closed

If user input "enter" key directly, bootmenu will be displayed.

* Control Auto Boot
```shell
By default, auto boot feature is enabled.

Command to disable auto boot:
$sudo fastboot oem autoboot 0
Command to enable auto boot:
$sudo fastboot oem autoboot 1

```

<a name="section-boot-order"></a>
HiKey supports booting from both eMMC and microSD card.

### Boot Sequence

If auto boot feature is enabled, a predefined boot entry will be selected
by default.

```shell
Boot entries:
1 -- Android Fastboot App
2 -- grub on eMMC
3 -- SD
```

If SD card is in slot, booting from SD (entry #3) is the highest priority. Otherwise, system will boot from eMMC (entry #2) instead.

If jumper on pin5-6 of J15 is connected, entry #1 is the highest priority instead.

Although booting from SD is higher priority than booting from eMMC (without jumper on pin5-6), user still can change the priority.
```shell
$sudo fastboot oem bootdevice [emmc|sd]
By default, SD card is the boot device. The boot flow is in below.
    a. If boot device is SD card.
         1) If SD card is present, boot from SD card.
         2) If SD card is _not_ present, boot from eMMC.
    b. If boot device is eMMC.
         Always boot from eMMC whether SD card is present or not. It's used when SD card is just mass storage device. For this case, people always leave SD card in slot.
```

## Set Serial Number in recovery mode

* make sure pin1-pin2 and pin3-pin4 on J15 are linked (recovery mode)

Name | Link | State
---- | ---- | -----
Auto Power up | Link 1-2 | closed
Boot Select | Link 3-4 | closed
GPIO3-1 | Link 5-6 | open

* Generate new serial number
```shell
# In recovery mode, we need to input this command to run initial program with fastboot protocol.
$sudo python hisi-idt.py --img1=l-loader.bin
# Change serial number by custom fastboot command.
$sudo fastboot oem serialno
```
By default, random serial number is generated in recovery mode. If user wants to generate new serial number, run the above command when hikey is in recovery mode. Then power off. In the next time, fastboot will use the new generated serial number for transmission.

* Set specified serial number
```shell
$sudo fastboot oem serialno set {serialno string, 16 characters long}
```

## Control user led

* Operate user led
```shell
$sudo fastboot oem led1 on
$sudo fastboot oem led1 off
```
All four leds could be controlled by fastboot command. They are from led1 to led4.

## How to Use UART0 as console

By default, UART3 is used as serial console. There's UART3 port on LS connector on HiKey. If user wants to use UART0 instead, he has to change a few things below.

* Use UART0 as console in ARM Trust Firmware
```shell
# Update $CROSS_COMPILE in uefi-tools/atf-build.sh
CROSS_COMPILE="$CROSS_COMPILE" make -j$NUM_THREADS PLAT="$ATF_PLATFORM" $SPD_OPTION DEBUG=$DEBUG CONSOLE=PL011_UART0_BASE CRASH_CONSOLE_BASE=PL011_UART0_BASE

# Rebuild ARM Trust Firmware
```

* Use UART0 as console in OP-TEE
```shell
# Rerun make with CFG_CONSOLE_UART=0 option
```

* Use UART0 as console in UEFI
```shell
# Update SERIAL_BASE in HisiPkg/HiKeyPkg/HiKey.dsc file.
DEFINE SERIAL_BASE = 0xF8015000

# Rebuild UEFI
```

* Use UART0 as console in kernel
```shell
# Update kernel command line in grub.cfg
linux ($root)/boot/Image console=tty0 console=ttyAMA0,115200 console=ttyAMA3,115200 root=/dev/disk/by-partlabel/system rootwait rw quiet efi=noruntime

# Don't need to build kernel again.
```

## Known Issues

* [ ] flashing l-loader.bin to the pseudopartitions is not enabled