RFID_JUKEBOX_MP3_FOLDER ?= ""

do_install:append () {


# this overwrite default settings with sample settings
cat <<EOF >${D}${SETTINGS_PATH}/cards.yaml
'1':
  package: volume
  plugin: ctrl
  method: set_volume
  kwargs:
    volume: 12
'2':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [12]
  ignore_same_id_delay: true
'3':
  package: volume
  plugin: ctrl
  method: set_volume
  args: 12
'4':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [12b]
'5':
  package: volume
  plugin: ctrl
  method: mute
'6':
  package: volume
  plugin: ctrl
  method: mute
  kwargs: {}
  ignore_card_removal_action: true
'7':
  package: volume
  plugin: ctrl
  method: get_volume
'8':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [18]
'7896':
  package: volume
  plugin: ctrl
  method: set_volume
  args:
  - 12
  - erasdf
  kwargs:
    aparam: 23
    bparam: sdfs
    cparam: 56
T:
  package: player
  plugin: ctrl
  method: play_card
  args: [path/to/music/folder]
G:
  alias: play_card
  args: path/to/music/folder
V+:
  alias: change_volume
  args: 3
  ignore_card_removal_action: true
V-:
  alias: change_volume
  args: -3
  ignore_card_removal_action: true
'1347557936':
  alias: play_single
  args:
  - 03_Jungle Boogie - Jungle Boogie.mp3
'1393947156':
  alias: play_single
  args:
  - Janis Joplin - Son Of A Preacher Man.mp3
EOF

#install mp3s
install -d -o pi ${D}${INSTALLATION_PATH}/shared/audiofolders
cp -r ${RFID_JUKEBOX_MP3_FOLDER} ${D}${INSTALLATION_PATH}/shared/audiofolders
chown -R pi: ${D}${INSTALLATION_PATH}/shared/audiofolders/*

}

# use local source instead of github
# inherit externalsrc
# EXTERNALSRC = "/home/tro/git/RPi-Jukebox-RFID"
# EXTERNALSRC_BUILD = "/home/tro/git/RPi-Jukebox-RFID"

    