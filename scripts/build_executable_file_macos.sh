#!/bin/bash
jpackage --input build/libs/ \
  --name SftpClient \
  --main-jar SftpClient.jar \
  --main-class com.sismo.demo.SftpClient \
  --type dmg \
  --java-options "-DSFTP_SERVER= \
                    -DSFTP_USER= \
                    -DSFTP_PHRASE= \
                    -DSFTP_PRIVATE_KEY=~/.ssh/id_rsa \
                    -DSFTP_SERVER_DIRECTORY=user_indicator \
                    -DSFTP_LOCAL_DIRECTORY=~/sftp-sismo"