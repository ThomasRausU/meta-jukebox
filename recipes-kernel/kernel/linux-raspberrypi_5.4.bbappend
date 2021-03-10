FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://wm8960.cfg \
			file://general.cfg"

			
LINUX_VERSION = "5.4.83"

SRCREV_machine = ""
SRCREV_kmeta = ""

SRCREV = "93349cdffc3fbb446c7c1fc7354215a5b8e30b97"