# Android-BadgeLayout
BadgeLayout provides an easy way of designing, creating and controlling badges within your Android apps. You pay no attention to any View issues during your development.

## Requirement

- Gradle

    ```gradle
    dependencies {
        compile 'com.cookizz:badgelib:1.1.1'
    }
    ```
- Maven

    ```xml
    <dependency>
      <groupId>com.cookizz</groupId>
      <artifactId>badgelib</artifactId>
      <version>1.1.1</version>
      <type>pom</type>
    </dependency>
    ```
  
## Usage

  1. If you are about to put badges on some views, first of all, wrap these views into `BadgeRelativeLayout` or `BadgeFrameLayout`. (No matter how deep they are being nested)

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

  2. In your `onCreate()` method, create a DotBadge on `@id/text_1` and a FigureBadge on `@id/text_2` using their default styles.

    ```java
    BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
    
    manager.createDotBadge(R.id.text_1, DotStyleNormal.class);
            .show();
    
    manager.createFigureBadge(R.id.text_2, FigureStyleNormal.class);
            .setFigure(45)
            .show();
    ```

  Here comes the result: 
  
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
        return new Point(750, 1334); // base on iPhone 6 resolution
    }

    @Override
    public Point getGravity() {
        return new Point(1, -1); // right-top of target view
    }

    @Override
    public Point getOffset() {
        return new Point(10, -10); // extrude towards right and top by 10 px based on its position resulted from getGravity()
    }
    
    @Override
    public int getTextSize() {
        return 24; // 24px text size in iPhone 6 resolution
    }

    @Override
    public int getTextColor(Context context) { // Point out figure text color.
        return context.getResources().getColor(R.color.white);
    }

    @Override
    public Typeface getTypeface(Context context) {
        return null; // use system
    }

    @Override
    public int getBackgroundColor(Context context) { // Point out the background color of your badge.
        return context.getResources().getColor(R.color.badge_red);
    }
    
    @Override
    public int getTerminalRadius() { // Point out the terminal radius when the badge displays as a fully round rect.
        return 18;
    }

    @Override
    public int getWidth(int figure) { // Point out the width in px in each case of the figure changes.
        if (figure < 0) {
            return 0;
        }
        final int divideBy10 = figure / 10;
        if (divideBy10 == 0) {
            return 36;
        } else if (divideBy10 < 10) {
            return 48;
        } else {
            return 62;
        }
    }

    @Override
    public String getText(int figure) { // Map figure into specified text
        String text;
        if (figure > 99) {
            text = "99+";
        } else {
            text = String.valueOf(figure);
        }
        return text;
    }

    @Override
    public boolean isVisible(int figure) { // Filter visibility of your badge in some cases of figure changes.
        return figure > 0;
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
