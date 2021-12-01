do_install:append () {
	install -d /home/root
	install -m 0755 ${WORKDIR}/share/dot.profile ${D}/home/root/.profile
	install -m 0755 ${WORKDIR}/share/dot.bashrc ${D}/home/root/.bashrc
}
