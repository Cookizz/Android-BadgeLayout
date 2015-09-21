# Android-BadgeLayout
Offers an easier way to design, create and control badges within your Android apps. You pay no attention to any `View` issues during your development.

![provided by designers from corp 21cn](https://github.com/Cookizz/Android-BadgeLayout/blob/master/badgedemo/src/main/res/raw/badgesample.png)

*provided by designers from corp 21cn*

## Usage
*For a simple implementation of this project see the `badgedemo/` folder.*

  1. Include `BadgeRelativeLayout` in your view. Generally, it should be a container of those target views who you intend to put badges on.
  
  (no ListView/ScrollView should be inside the container, they'll be supported later on)
  
        <com.cookizz.badgelib.BadgeRelativeLayout
            android:id="@+id/badge_manager"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            
            <Button
                android:id="@+id/badge_target"
                android:text="@string/click_me"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>

        </com.cookizz.badgelib.BadgeRelativeLayout>

  2. In your `onCreate()` method, create a badge attached on `@id/badge_target`.

        /**
         * Create a normal figure badge attached on your target.
         * FigureStyleNormal is a built-in figure badge style.
         * You can customize your own style by creating a subclass of FigureStyleTemplate.
         */
        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = manager.createFigureBadge(R.id.badge_target, FigureStyleNormal.class);
        
        // control your badge independent from `View` hierarchy
        badge.show();
        badge.setFigure(45);
    
  3. Once you obtained a badge reference and called its `show()` method, the following control accesses are now available for you.

        void badge.hide();
        boolean isShown();
        
        // Detach a badge from its target view, then the badge reference are disabled forever.
        void badge.detach();
        // Detect whether your badge is still available.
        boolean badge.isAttached();
        
        // A badge typed "Figure" has an access to set its figure.
        void badge.setFigure(int);
        int badge.getFigure();
        
        // Get the style info of your badge.
        BadgeStyle getStyle();

## Customize
  
  To customize your style of badge is quite easy.
  
  1. create a subclass of `FigureStyleTemplate` or `DotStyleTemplate` and put your design into it.
  
        public class MyFigureStyle extends FigureStyleTemplate {
        
            @Override
            public Point getReferencedScreenResolution() {
                /** 
                 * You must point out which screen resolution your design is based on.
                 * The base class will automatically complete screen adaption in the runtime.
                 * Point's x value indicates the width pixels of the screen,
                 * y, of course, the height.
                 */
            }
        
            @Override
            public Point getGravity() {
                /**
                 * Tell the base class which direction your badge goes
                 * when it's going to be attached on the target view.
                 * Point's x value indicates the x direction, y, of course, the y direction.
                 * For instance, (1, -1) shows that it will be put adjacent
                 * to the right-top corner of the target view.
                 * (0, 0) shows that it will be put at the center.
                 */
            }
        
            @Override
            public Point getOffset() {
                /** After the gravity has been set, you can set an extra offset the badge will go.
                 * Point's x value indicates the x offset, y, of course, the y offset.
                 */
            }
            
            @Override
            public int getTextSize() {
                // Point out the size of figure text.
            }
        
            @Override
            public int getTextColor(Context context) {
                // Point out the color of figure text.
            }
        
            @Override
            public Typeface getTypeface(Context context) {
                // Point out the typeface of figure text.
            }
        
            @Override
            public int getBackgroundColor(Context context) {
                // Point out the background of the badge's background.
            }
        
            @Override
            public int getTerminalRadius() {
                // Point out the terminal radius when the badge displays as a fully round rect.
            }
        
            @Override
            public int getWidth(int figure) {
                // Point out the width relative to specified figure value.
            }
        
            @Override
            public String getText(int figure) {
                // Point out the intrinsic text relative to specified figure value.
            }
        
            @Override
            public boolean isVisible(int figure) {
                // Point out in which case the badge should be visible
                // relative to specified figure value.
            }
        }
    
  2. Use your `MyFigureStyle` in badge creation.

        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = manager.createFigureBadge(R.id.badge_target, MyFigureStyle.class);

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
