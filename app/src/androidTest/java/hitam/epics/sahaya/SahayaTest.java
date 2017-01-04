package hitam.epics.sahaya;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SahayaTest {

    @Rule
    public ActivityTestRule<Sahaya> mActivityTestRule = new ActivityTestRule<>(Sahaya.class);

    @Test
    public void sahayaTest() {
        ViewInteraction button = onView(
                allOf(withText("Sign In"), isDisplayed()));
        button.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.login_email),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.login_email),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        editText2.perform(replaceText("sanjit291196@gmail.com"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.login_email), withText("sanjit291196@gmail.com"),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        editText3.perform(pressImeActionButton());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.login_password),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        editText4.perform(replaceText("san123"), closeSoftKeyboard());

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.login_password), withText("san123"),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        editText5.perform(pressImeActionButton());

        ViewInteraction button2 = onView(
                allOf(withText("Sign In"),
                        withParent(withId(R.id.login_form)),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Try Again")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withText("New here? Sign Up"), isDisplayed()));
        button3.perform(click());

        ViewInteraction editText6 = onView(
                withId(R.id.registration_name));
        editText6.perform(scrollTo(), click());

        ViewInteraction editText7 = onView(
                withId(R.id.registration_name));
        editText7.perform(scrollTo(), replaceText("Sanjit Singh Chouhan"), closeSoftKeyboard());

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.registration_name), withText("Sanjit Singh Chouhan")));
        editText8.perform(pressImeActionButton());

        ViewInteraction editText9 = onView(
                withId(R.id.registration_email));
        editText9.perform(scrollTo(), replaceText("sanjit291196@gmail.com"), closeSoftKeyboard());

        ViewInteraction editText10 = onView(
                allOf(withId(R.id.registration_email), withText("sanjit291196@gmail.com")));
        editText10.perform(pressImeActionButton());

        ViewInteraction editText11 = onView(
                withId(R.id.registration_password));
        editText11.perform(scrollTo(), replaceText("san123"), closeSoftKeyboard());

        ViewInteraction editText12 = onView(
                allOf(withId(R.id.registration_password), withText("san123")));
        editText12.perform(pressImeActionButton());

        ViewInteraction editText13 = onView(
                withId(R.id.registration_confirm_password));
        editText13.perform(scrollTo(), replaceText("san123"), closeSoftKeyboard());

        ViewInteraction editText14 = onView(
                allOf(withId(R.id.registration_confirm_password), withText("san123")));
        editText14.perform(pressImeActionButton());

        ViewInteraction editText15 = onView(
                withId(R.id.registration_phone));
        editText15.perform(scrollTo(), replaceText("7382978847"), closeSoftKeyboard());

        ViewInteraction editText16 = onView(
                allOf(withId(R.id.registration_phone), withText("7382978847")));
        editText16.perform(pressImeActionButton());

        ViewInteraction editText17 = onView(
                withId(R.id.registration_occupation));
        editText17.perform(scrollTo(), replaceText("Student"), closeSoftKeyboard());

        ViewInteraction editText18 = onView(
                allOf(withId(R.id.registration_occupation), withText("Student")));
        editText18.perform(pressImeActionButton());

        ViewInteraction editText19 = onView(
                withClassName(is("android.widget.EditText")));
        editText19.perform(scrollTo(), replaceText("NiL"), closeSoftKeyboard());

        ViewInteraction editText20 = onView(
                withText("NiL"));
        editText20.perform(pressImeActionButton());

        ViewInteraction button4 = onView(
                withText("Register"));
        button4.perform(scrollTo(), click());

        ViewInteraction button5 = onView(
                allOf(withText("Who we are?"), isDisplayed()));
        button5.perform(click());

        ViewInteraction button6 = onView(
                allOf(withText("Donate"), isDisplayed()));
        button6.perform(click());

    }

}
