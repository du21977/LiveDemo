

cmake_minimum_required(VERSION 3.4.1)

include_directories(./libs/x264/include
                    ./libs/faac/include
                    ./libs/rtmpdump
                    ./libs/rtmpdump/librtmp
                    ./libs/rtmpdump/polarssl
                   )

link_directories(./libs/${ANDROID_ABI})

find_library(
              log-lib
              log )
find_library(android-lib
             android)


add_library(
            dn_live
            SHARED
            src/main/cpp/dn_live.c
            src/main/cpp/queue.c
            )

target_link_libraries(dn_live
                       ${log-lib}
                       ${android-lib}
                       x264
                       rtmp
                       faac
                       )

