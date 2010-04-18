
import java.awt.Rectangle;

public class TrackedObject extends Rectangle {

   public int color;

   public TrackedObject(int x, int y, int width, int height, int color) {

      super(x, y, width, height);
      this.color = color;
   }

   public Rectangle getCenter() {
      return new Rectangle(this.x + (this.width / 2), this.y + (this.height / 2), 0, 0);
   }

   public int getArea() {

      return this.width * this.height;
   }

   public boolean equals(Object another) {

      TrackedObject rect = (TrackedObject) another;
      if (this.x != rect.x)
         return false;
      if (this.y != rect.y)
         return false;
      if (this.width != rect.width)
         return false;
      if (this.height != rect.height)
         return false;
      return true;
   }

   public boolean isNull() {

      if (this == null)
         return true;

      if (isEmpty())
         return true;

      return false;
   }
} 