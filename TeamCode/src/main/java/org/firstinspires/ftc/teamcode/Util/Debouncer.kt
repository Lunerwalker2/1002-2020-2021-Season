package org.firstinspires.ftc.teamcode.Util

class Debouncer(var toggle: () -> Boolean) {

    var bounce = false
    var changed = false

    var oneAction = {}
    var twoAction = {}



    fun check(){
        if (toggle.invoke() && !bounce) {
            control()
            bounce = true
        } else if (!toggle.invoke()) {
            bounce = false
        }
    }

    fun control() {
        if (!changed) {
            oneAction.invoke()
            changed = true
        } else if (changed) {
            twoAction.invoke()
            changed = false
        }
    }
}