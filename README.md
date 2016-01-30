# Android-BadgeLayout
BadgeLayout provides an easier way of designing, creating and controlling badges within your Android apps. You pay no attention to any View issues during your development.

## Requirement
  
  1. Android API 9 or higher.
  2. Add [badge.jar](https://github.com/Cookizz/Android-BadgeLayout/blob/master/release/badge.jar) to your file dependencies.
  
## Usage

  1. Include `com.cookizz.badge.BadgeRelativeLayout` or `com.cookizz.badge.BadgeFrameLayout` in your layout. Wrap your target views that you intend to put badges on.

  Here I take `BadgeRelativeLayout` as an example:

```xml
        <com.cookizz.badge.BadgeRelativeLayout
            android:id="@+id/badge_layout"
            ...
            ...>
            
            <TextView
                android:id="@+id/text_1"
                android:text="@string/dot"
                ...
                .../>

            <TextView
                android:id="@+id/text_2"
                android:text="@string/figure"
                ...
                .../>
                
        </com.cookizz.badge.BadgeRelativeLayout>
```

  In no matter which view hierarchy in `BdadeRelativeLayout` will it be OK for your targets to put except ScrollView and ListView, which may be supported later on.

  2. In your `onCreate()` method, create a DotBadge on `@id/text_1` and a FigureBadge on `@id/text_2` from your `BadgeRelativeLayout` using their default styles.

```java
        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        
        manager.createDotBadge(R.id.text_1, DotStyleNormal.class);
                .show();
        
        manager.createFigureBadge(R.id.text_2, FigureStyleNormal.class);
                .setFigure(45)
                .show();
```

  Then here comes the result: 
  
  ![code effect](http://7xawtr.com1.z0.glb.clouddn.com/dot_and_figure_badge.png)
    
  3. Once you obtained a badge reference by calling its `show()` method, the following control accesses to DotBadge/FigureBadge are now available for you.

```java
        /**
         * Interfaces in common
         */
        void hide();
        boolean isShown();
        void detach();
        boolean isAttached();
        BadgeStyle getStyle();

        /**
         * FigureBadge exclusive
         */
        void setFigure(int);
        int getFigure();
```

## Design your badge style
  Quite easy! Take `FigureBadge` as an example...
  
  1. Create a subclass of `FigureStyle` and implement your design.
  
```java
public class MyFigureStyle extends FigureStyle {

    @Override
    public Point getReferencedScreenResolution() {
        // Point out which screen resolution your design is based on.
    }

    @Override
    public Point getGravity() {
        // Tell the base class which direction your badge goes.
    }

    @Override
    public Point getOffset() {
        // After settings gravity, you may set an extra offset in px the badge will go.
    }
    
    @Override
    public int getTextSize() {
        // Point out figure text size.
    }

    @Override
    public int getTextColor(Context context) {
        // Point out figure text color.
    }

    @Override
    public Typeface getTypeface(Context context) {
        // Point out figure text typeface.
    }

    @Override
    public int getBackgroundColor(Context context) {
        // Point out the background color of your badge.
    }
    
    @Override
    public int getTerminalRadius() {
        // Point out the terminal radius when the badge displays as a fully round rect.
    }

    @Override
    public int getWidth(int figure) {
        // Point out the width in px in each case of the figure changes.
    }

    @Override
    public String getText(int figure) {
        // Map figure into specified text, such as I/II/IV/VI.
    }

    @Override
    public boolean isVisible(int figure) {
        // Filter visibility of your badge in some cases of figure changes.
    }
}
```
    
  2. Put your design into use.

```java
        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = manager.createFigureBadge(R.id.badge_target, MyFigureStyle.class)
                .setFigure(45)
                .show();
```

## Limitations
  
  * Not supporting "any-text" badge so far, coming soon.
  
  * No touching event available on the badge, coming soon.
  
  * If you intend to put a badge into a list item or into a ScrollView, do not wrap the list or the ScrollView into BadgeRelativeLayout. Contrarily, put BadgeRelativeLayout into their View tree.

## Developed By

  * Cookizz - <com.cookizz@gmail.com>

## License

    Copyright 2015 Cookizz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
