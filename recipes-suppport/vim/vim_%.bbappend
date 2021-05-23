FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://vimrc" 

do_install_append() {   
	install -d ${D}/home/root
    install -m 0644 ${WORKDIR}/vimrc ${D}/home/root/.vimrc
}
FILES_${PN} += "/home/root*"