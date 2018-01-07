package com.moodi.breakyourgoal.addgoal

import dagger.Component
import javax.inject.Singleton

/**
 * Created by ningappamoodi on 16/9/17.
 */

@Component(
       modules =
       [(AddGoalModule::class)]

    )
interface AddGoalComponent {

    fun inject(presenter: AddGoalPresenterImpl)
}