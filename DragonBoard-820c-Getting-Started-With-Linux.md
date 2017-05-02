This page has instructions to get started with Debian or OpenEmbedded on the DragonBoard 820c board.

# Disclaimer 
DragonBoard 820c is not yet commercially available and is in beta testing only. Please expect limited documentation and support until further notice.

# Bootloaders

Throughout these instructions, we are assuming that you have been able to flash the board with an initial build from Qualcomm such that you can boot the board into fastboot. If you cannot get the board to boot into fastboot, then you need to get in touch with the person that provided you with the board.

In recent bootloaders from Qualcomm, we noticed that support for splash screen was enabled in LK (in Android builds), so that the snapdragon logo can be displayed very early in the boot process.

The splash screen support in bootloader creates issues with the linux kernel, as we don't have proper handoff of resources between LK and kernel as of yet. As such when using Linux releases, and especially the Linaro kernel, we need to explicitly disable display in LK. If it is enabled you will notice that the kernel fails to boot and abruptly returns into SBL (reboots). To disable display support in LK, put the board into fastboot mode and run

    fastboot oem select-display-panel none

By default it should be 'hdmi', and you can thus revert to default settings (if you need to run Android) with the following command:

    fastboot oem select-display-panel hdmi

After you set it to none, please power off/reboot and try booting linux kernel again.

# Onboard storage

The onboard storage is partionned such as : 

* `/dev/sda9` is `userdata` and is ~24GB
* `/dev/sde18` is `system` and is ~3GB
* `/dev/sde17` is `boot`

For now there is no rescue tool which is provided, so it is not recommended to change the partition layout. The root file system can be installed in `userdata` or `system` based on how much space is needed. It is even possible to install a Debian image in `userdata` and an OpenEmbedded image in `system` partition.

# Installing Debian

## Console (no graphics) image

Debian builds for the DB820c can be found here: http://builds.96boards.org/snapshots/dragonboard820c/linaro/debian/. They have for now has minimal features set (mostly console, UFS, 4 core running at the lowest speed). Features will be added in this builds stream. Note that kernel version might changes regularly and without notice until mid 2017.

To install the Debian root file system:

1. Download either the `developer`` image from the link above
1. Uncompress the root file system image
1. Flash the image into `userdata` (or `system`).

So, assuming you are trying to use the latest build:

    wget http://builds.96boards.org/snapshots/dragonboard820c/linaro/debian/latest/linaro-stretch-developer-qcom-snapdragon-arm64-*.img.gz
    gunzip linaro-stretch-developer-qcom-snapdragon-arm64-*.img.gz
    fastboot flash userdata linaro-stretch-developer-qcom-snapdragon-arm64-*.img

You can download the prebuilt boot image as well, from the same location. However note that the boot image is by default going to try to mount the file system on `rootfs` partition, like on DragonBoard 410c, so you need to update the boot image before flashing it, since we do not (yet) use the `rootfs` partition on DB820c:

    wget http://builds.96boards.org/snapshots/dragonboard820c/linaro/debian/latest/boot-linaro-stretch-qcom-snapdragon-arm64-*.img.gz
    gunzip boot-linaro-stretch-qcom-snapdragon-arm64-*.img.gz
    abootimg -u boot-linaro-stretch-qcom-snapdragon-arm64-*.img -c "cmdline=root=/dev/disk/by-partlabel/userdata rw rootwait console=tty0 console=ttyMSM0,115200n8"

You might need to replace `userdata` with `system`, of course.

## Graphical image (with GPU)

Support for the Adreno A5xx GPU found in the Snapdragon 820 SoC has started in mesa open source driver. The support is fairly new and experimental, and all the development for A5xx happens on the development/master branch of mesa. Mesa 17.0 was the first release with initial GPU support. However mesa 17.0 is not available in the stable and/or testing version of Debian (e.g. Jessie or Stretch) and it will only be available in the next version of Debian. 

However mesa 17.0 is available in the `experimental` Debian repositories. As such, you want to experiment with Graphics on Debian, the recommended method is the following:

1. Start with the console/headless image from previous section
1. Upgrade the Debian root file system to Debian `sid` to get the latest version of all components and install a desktop environment.
1. Upgrade mesa from Debian `experimental`
1. Install the GPU firmware

Once your board is running with the Debian minimal/console image as per previous section, you can upgrade to Debian `sid`: edit the file `/etc/apt/sources.list` 

    $ cat /etc/apt/sources.list
    deb http://http.debian.net/debian/ sid main contrib non-free
    deb http://http.debian.net/debian/ experimental main 

You can remove all previous content. Then run, as root:

    apt update
    apt dist-upgrade
    apt install lxqt

Of course you need to make sure that Ethernet is working, as you will download everything from the main Debian archives. Once these commands finish, you are running Debian `sid` and you have installed the LxQt desktop environment (you can install any other desktop, but LxQt is the reference desktop used in all Linaro builds). 

Now the you can install `mesa` from the `experimental` repositories:

    apt install -t experimental `dpkg -l | grep mesa | cut -d' ' -f3`

Finally the last step is to install the GPU firmware files. These files are not distributed publicly by Qualcomm nor Linaro at this point (like the bootloaders). You need to acquire them, typically from whoever you received the board.. They need to be installed in `/lib/firmware/` folder on the board.

If all steps went fine, you should now have a Debian desktop with working GPU. You can start X manually and run graphical applications:

    X&
    export DISPLAY=:0
    glxgears &
    xterm &

Or you can start the entire desktop using LxQt default login manager:

    systemctl start sddm

# Installing an Open Embedded based image

Initial support for DragonBoard 820c has been added into the OpenEmbedded QCOM BSP later, including the appropriate kernel recipe. To build an image for Dragonboard 820c , simply follow the same instructions as usual, from [Dragonboard-410c-OpenEmbedded-and-Yocto](https://github.com/Linaro/documentation/blob/master/Reference-Platform/CECommon/OE.md). When you select the MACHINE to build for, pick `dragonboard-820c`.

The board is being added to the Linaro Reference Platform OpenEmbedded builds, and prebuilt images for this board should appear in the coming days here : http://builds.96boards.org/snapshots/reference-platform/openembedded/.

# Kernel source code

The Linux kernel used for DragonBoard 820c can be found in the [Linaro Qualcomm Landing Team git repository](https://git.linaro.org/landing-teams/working/qualcomm/kernel.git). For now the support for this board is preliminary and can only be found in either 'release' branches named as `release/db820c/qcomlt-x.y` (the latest one being the most up-to-date/recent) or the `integration-linux-qcomlt` branch, which is regularly rebased on recent mainline, and is used for developers.

    git: http://git.linaro.org/landing-teams/working/qualcomm/kernel.git
    branch: release/db820c/qcomlt-x.y or integration-linux-qcomlt
    defconfig: arch/arm64/defconfig kernel/configs/distro.config

To build the Linux kernel, you can use the following instructions:

    git clone -n http://git.linaro.org/landing-teams/working/qualcomm/kernel.git
    cd kernel
    git checkout -b db820c <remote branch>
    export ARCH=arm64
    export CROSS_COMPILE=<path to your GCC cross compiler>/aarch64-linux-gnu-
    make defconfig distro.config
    make -j4 Image dtbs KERNELRELEASE=`make kernelversion`-linaro-lt-qcom

Additionally, you might want or need to compile the kernel modules:

    make -j4 modules KERNELRELEASE=`make kernelversion`-linaro-lt-qcom

To boot the kernel image, you will need a fastboot compatible boot image, and you can refer to [[Dragonboard-Boot-Image]] for instructions to create such an image.