do_install:append () {
# this overwrite default settings
cat <<EOF >${D}${SETTINGS_PATH}/rfid.yaml
rfid:
  readers:
    read_00:
      module: rc522_spi
      config:
        spi_bus: 0
        spi_ce: 0
        pin_irq: 24
        pin_rst: 25
        mode_legacy: false
        antenna_gain: 4
        log_all_cards: true
      same_id_delay: 1.0
      log_ignored_cards: false
      place_not_swipe:
        enabled: false
        card_removal_action:
          alias: pause
rfid.pinaction.rpi:
  enabled: false
  pin: 0
  duration: 0.2
  retrigger: true
EOF

cat <<EOF >${D}${SETTINGS_PATH}/gpio.yaml

EOF
}
    