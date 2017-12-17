package com.moodi.breakyourgoal

import android.app.Application
import com.moodi.breakyourgoal.addgoal.AddGoalComponent
import com.moodi.breakyourgoal.addgoal.AddGoalModule
import com.moodi.breakyourgoal.addgoal.DaggerAddGoalComponent

/**
 * Created by ningappamoodi on 16/9/17.
 */
class GoalApplication: Application {


    val component: AddGoalComponent by lazy {

        DaggerAddGoalComponent.builder().addGoalModule(AddGoalModule(this)).build()
    }

    constructor()
}