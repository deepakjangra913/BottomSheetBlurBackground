# Blur bottom sheet background
Steps to implement this in your project
> Step 1. Add the JitPack repository to your build file
```gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
> Step 2. Add the dependency
```gradle 
dependencies {
		implementation 'com.github.deepakjangra913:BottomSheetBlurBackground:Version'
	}
```


> Step 3 Add the view in activity from where yours bottom sheet will open 
```gradle 
 <!--We need to define this view in our activity to blur background-->
        <View
            android:id="@+id/view_container"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_alignParentTop="true"
            android:layout_alignParentBottom="true"
            android:layout_alignParentEnd="true"
            android:layout_alignParentStart="true"/>
```
> Step 4 Implement the base bottom sheet fragment by your child fragment 
```gradle 

class ChildBottomSheetFragment : BaseBottomSheetFragment<FragmentChildBottomSheetBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentChildBottomSheetBinding {
        return FragmentChildBottomSheetBinding.inflate(inflater, container, attachToParent)
    }

    override fun initViews() {
        setBlurRadius(10f)
        setBlurredContainerId(R.id.view_container)
    }
}
```
When you open your child fragment then background will look like this 

![Screenshot_20240525_125807](https://github.com/deepakjangra913/BlurBackgroundPractice/assets/106225128/766dc812-67a8-477b-a39a-5a6db5ea33e4)

Thats it in this way you can implement this blur bottom sheet. Hopefully it would be helpful for you  
