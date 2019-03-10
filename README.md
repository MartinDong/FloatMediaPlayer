## Android Music Player (音乐播放器)

- 全局浮动按钮控制播放器，即便是页面跳转（除后台外）
- 极简 MVP 模式
- 使用网络三件套，OkHttp、Retrofit、RxJava
- 仿照 Glide 编写的图片加载库


#### 主动请求音频控制焦点，暂停其他的多媒体播放
```kotlin
private var mAudioManager: AudioManager? = null

mAudioManager!!.requestAudioFocus(
                    mAudioFocusListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
```

#### 监听音频焦点改变，控制当前的播放器暂停
```kotlin
private var mAudioFocusListener: AudioManager.OnAudioFocusChangeListener? = null

mAudioFocusListener = AudioManager.OnAudioFocusChangeListener {
            if (it == AudioManager.AUDIOFOCUS_LOSS) {
                //暫停逻辑
                pause()
            } else if (it == AudioManager.AUDIOFOCUS_GAIN) {
                //播放逻辑
                play(mCurrentSong)
            }
        }
```