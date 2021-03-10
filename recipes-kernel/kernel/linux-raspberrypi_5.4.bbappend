FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://wm8960.cfg \
			file://general.cfg"

			
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-raspberrypi-5.4:"

LINUX_VERSION = "5.4.83"

SRCREV_machine = ""
SRCREV_kmeta = ""

SRCREV = "93349cdffc3fbb446c7c1fc7354215a5b8e30b97"

SRC_URI = "\
    git://github.com/raspberrypi/linux.git;branch=${LINUX_RPI_BRANCH} \
"

KERNEL_DEVICETREE = " \
    ${RPI_KERNEL_DEVICETREE} \
    ${RPI_KERNEL_DEVICETREE_OVERLAYS} \
"

#RPI_KERNEL_DEVICETREE = " \
#    broadcom/bcm2708-rpi-zero-w.dtb \
#"

RPI_KERNEL_DEVICETREE_OVERLAYS += " \
    overlays/wm8960-soundcard.dtbo \
"