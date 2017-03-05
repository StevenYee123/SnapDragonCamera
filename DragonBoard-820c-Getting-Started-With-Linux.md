This page has instructions to get started with Debian or OpenEmbedded on the DragonBoard 820c board.

# Disclaimer 
DragonBoard 820c is not yet commercially available and is in beta testing only. Please expect limited documentation and support until further notice.

# Bootloaders

Throughout these instructions, we are assuming that you have been able to flash the board with an initial build from Qualcomm such that you can boot the board into fastboot. If you cannot get the board to boot into fastboot, then you need to get in touch with the person that provided you with the board.

# Onboard storage

The onboard storage is partionned such as : 

* `/dev/sda9` is `userdata` and is ~24GB
* `/dev/sde18` is `system` and is ~3GB
* `/dev/sde17` is `boot`

For now there is no rescue tool which is provided, so it is not recommended to change the partition layout. The root file system can be installed in `userdata` or `system` based on how much space is needed. It is even possible to install a Debian image in `userdata` and an OpenEmbedded image in `system` partition.

# Installing Debian

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

# Installing an Open Embedded based image

Initial support for DragonBoard 820c has been added into the OpenEmbedded QCOM BSP later, including the appropriate kernel recipe. To build an image for Dragonboard 820c , simply follow the same instructions as usual, from [Dragonboard-410c-OpenEmbedded-and-Yocto](https://github.com/Linaro/documentation/blob/master/Reference-Platform/CECommon/OE.md). When you select the MACHINE to build for, pick `dragonboard-820c`.

The board is being added to the Linaro Reference Platform OpenEmbedded builds, and prebuilt images for this board should appear in the coming days here : http://builds.96boards.org/snapshots/reference-platform/openembedded/.

# Kernel source code

The Linux kernel used for DragonBoard 820c can be found in the [Linaro Qualcomm Landing Team git repository](https://git.linaro.org/landing-teams/working/qualcomm/kernel.git). For now the support for this board is preliminary and can only be found in either 'release' branches named as `db820c/qcomlt-x.y` (the latest one being the most up-to-date/recent) or the `integration-linux-qcomlt` branch, which is regularly rebased on recent mainline, and is used for developers.

    git: http://git.linaro.org/landing-teams/working/qualcomm/kernel.git
    branch: db820c/qcomlt-x.y or integration-linux-qcomlt
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