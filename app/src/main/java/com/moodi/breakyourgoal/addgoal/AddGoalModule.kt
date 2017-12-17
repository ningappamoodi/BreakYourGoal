package com.moodi.breakyourgoal.addgoal

import com.moodi.breakyourgoal.GoalApplication
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by ningappamoodi on 16/9/17.
 */

@Module
class AddGoalModule {

    var  goalApp: GoalApplication? = null


    public constructor( goalApp: GoalApplication)  {

        this.goalApp = goalApp
    }

        @Provides fun provideAddGoalImpl(): AddGoalI {

        return AddGoalImpl()
    }
}