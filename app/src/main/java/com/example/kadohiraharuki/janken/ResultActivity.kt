package com.example.kadohiraharuki.janken

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val gu = 0
    val choki = 1
    val pa = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val id = intent.getIntExtra("MY_HAND", 0)

        val myHand: Int
        myHand = when (id) {
            R.id.gu -> {
                myHandImage.setImageResource(R.drawable.gu)
                gu
            }
            R.id.choki -> {
                myHandImage.setImageResource(R.drawable.choki)
                choki
            }
            R.id.pa -> {
                myHandImage.setImageResource(R.drawable.pa)
                pa
            }
            else -> gu
        }
        //コンピュータの手を決める、共有プリファレンスを読み出し最強ジャンケンアプリへ
        val comHand = getHand()
        when (comHand) {
            gu -> comHandImage.setImageResource(R.drawable.com_gu)
            choki -> comHandImage.setImageResource(R.drawable.com_choki)
            pa -> comHandImage.setImageResource(R.drawable.com_pa)
        }
        //勝利を判定する
        val gameResult = (comHand - myHand + 3) % 3
        when (gameResult) {
            0 -> resultLabel.setText(R.string.result_draw) //引き分け
            1 -> resultLabel.setText(R.string.result_win)//勝利
            2 -> resultLabel.setText(R.string.result_lose)//敗北
        }

        backButton.setOnClickListener { finish() }

        //ジャンケンの結果をsaveData関数を読み出し保存
        saveData(myHand, comHand, gameResult)
    }

    private fun saveData(myHand: Int, ComHand: Int, gameResult: Int) {
        //デフォルトの共有プリファレンスを取得する
        //関数内では拡張元のクラスのインスタンスをthisとして扱うことができる. preference:優先
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //共有プリファレンスから値を取得、getIntメソッドとを使用し、各種変数に代入
        //ゲームのカウント数を記録、引き数がない場合０を返す
        val gameCount = pref.getInt("Game_COUNT", 0)
        //連勝数を記録
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        //コンピュータの手を記録
        val LastComHand = pref.getInt("Last_COM_HAND", 0)
        //ゲームの結果を記録、結果の記録は0,1,2の値を取る為返り直-1
        val LastGameResult = pref.getInt("GAME_RESULT", -1)

        //SharedPreferences.Editorインターフェイスのインスタンスを取得する
        val editor = pref.edit()
        //putIntメソッドで+1して保存
        editor.putInt("GAME_COUNT", gameCount + 1)
                //コンピュータが連勝したかどうか調べる
                .putInt("WINNING_STREAK_COUNT",
                        if (LastGameResult == 2 && gameResult == 2)
                            winningStreakCount + 1
                        else
                            0)
                //引数として受け取った今回の結果と前回の結果を共有プリファレンスに書き込み
                .putInt("Last_MY_HAND", myHand)
                .putInt("LAST_COM_HAND", ComHand)
                .putInt("BEFORE_LAST_COM_HAMD", LastComHand)
                .putInt("GAME_RESULT", gameResult)
                //applyメソッドで保存
                .apply()
    }

    private fun getHand(): Int {
        //var 変数の値が変わるときに使用
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //共有プリファレンスから値を取得、getIntメソッドとを使用し、各種変数に代入
        //ゲームのカウント数を記録、引き数がない場合０を返す
        val gameCount = pref.getInt("Game_COUNT", 0)
        //連勝数を記録
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        //プレイヤーの手を記録
        val LastMyHand = pref.getInt("Last_MY_HAND", 0)
        //コンピュータの手を記録
        val LastComHand = pref.getInt("Last_COM_HAND", 0)
        //前々回のコンピュータの手を記録
        val beforeLastComHand = pref.getInt("BEFORE_Last_COM_HAND", 0)
        //ゲーム結果
        val gameResult = pref.getInt("GAME_RESULT", -1)

        //前回の勝負が1回目でコンピュータが勝ったら、コンピュータは次に出す手を変える
        if (gameCount == 1) {
            if (gameResult == 2) {
                //前回のコンピュータの手と今回のコンピュータの手が同じになるまで無限ループ
                while (LastComHand == hand) {
                    //0~1の乱数を作成し3をかけ、int型にキャスト
                    hand = (Math.random() * 3).toInt()
                }

                //前回の勝負が1回目で、コンピュータが負けた場合、相手の出した手に勝つ手
            } else if (gameResult == 1) {
                hand = (LastMyHand - 1 + 3) % 3
            }
            //同じ手で連勝した場合は手を変える
        } else if (winningStreakCount > 0) {
            if (beforeLastComHand == LastComHand) {
                while (LastComHand == hand) {
                    hand == (Math.random() * 3).toInt()
                }
            }
        }
        return hand
    }
        //以下自分で作成（途中で断念）
        /*private fun Datasave(myHand:Int, comHand:Int, gameResult:Int){
        val prefs = getSharedPreferences(PREFERENCES_KEY, AppCompatActivity.MODE_PRIVATE)
        val memo : Int = prefs.getInt(DATA1_KEY)
        val game_count:Int
        val win_count:Int
        val last_my_hand:Int
        val last_last_com_hand:Int
        }
        */

}
