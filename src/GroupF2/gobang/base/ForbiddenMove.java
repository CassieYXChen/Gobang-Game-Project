package GroupF2.gobang.base;

import GroupF2.gobang.chessboard.ChessClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

import java.util.List;

public class ForbiddenMove {

    public static boolean forbiddenMove(List<ChessClass> chessClassList, ChessClass chessClass, boolean[][] arr, Color[][] colors) {
        if (chessClassList.size() < 5 || chessClass.getColor() == Color.WHITE) {
            return false;
        }
        // The current number of connected children
        int live3 = 0;
        int live4 = 0;
        int count = 1;
        int left = 0;
        int right = 0;
        int lblank = 0;
        int rblank = 0;
        // There are several links on the left
        for (int i = chessClass.getX() - 1; i >= 0 && i >= chessClass.getX() - 4; i--) {
            if (arr[i][chessClass.getY()] && chessClass.getColor().equals(colors[i][chessClass.getY()])) {
                count++;
                left++;
            } else if (i>=2 && !arr[i][chessClass.getY()] && arr[i-1][chessClass.getY()] && lblank==0 && chessClass.getColor().equals(colors[i-1][chessClass.getY()])) {
                lblank++;
                count++;
                left++;
                i--;
            } else {
                break;
            }
        }
        // On the right hand side there are several links
        for (int i = chessClass.getX() + 1; i <= 19  && i <= chessClass.getX() + 4; i++) {
            if (arr[i][chessClass.getY()] && chessClass.getColor().equals(colors[i][chessClass.getY()])) {
                count++;
                right++;
            }else if (i<=17 && !arr[i][chessClass.getY()] && arr[i+1][chessClass.getY()] && lblank==0 && rblank == 0 && chessClass.getColor().equals(colors[i+1][chessClass.getY()])) {
                rblank++;
                count++;
                right++;
                i++;
            }
            else {
                break;
            }
        }
        // The two ends of the horizontal line are empty
        if (count == 3 && (chessClass.getX() - left -lblank - 1>=0) &&!arr[chessClass.getX() - left -lblank - 1][chessClass.getY()]  && (chessClass.getX() + right + rblank <=18) && !arr[chessClass.getX() + right + rblank + 1][chessClass.getY()]) {
            live3++;
            System.out.println("hengxian");
            if(live3>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"双活三\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // The two ends of the four horizontal lines are empty
        if (count == 4 && (chessClass.getX() - left -lblank - 1>=0) &&!arr[chessClass.getX() - left -lblank - 1][chessClass.getY()]  && (chessClass.getX() + right + rblank <=18) && !arr[chessClass.getX() + right + rblank + 1][chessClass.getY()]) {
            live4++;
            System.out.println("hengxian");
            if(live4>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"活四\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Whether the horizontal line is five
        if (count > 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"长连\" —— Forbidden Move");
            alert.setTitle("Warning");
            alert.setHeaderText("Warning: ");
            ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(confirmButton);
            alert.show();
            return true;
        }
        count = 1;
        left = 0;
        right = 0;
        lblank = 0;
        rblank = 0;

        // There are several links on the vertical
        for (int i = chessClass.getY() - 1; i >= 0 && i >= chessClass.getY() - 4; i--) {
            if (arr[chessClass.getX()][i] && chessClass.getColor().equals(colors[chessClass.getX()][i])) {
                left++;
                count++;
            }else if (i>=2 && !arr[chessClass.getX()][i] && arr[chessClass.getX()][i-1] && lblank==0 && chessClass.getColor().equals(colors[chessClass.getX()][i-1])) {
                lblank++;
                count++;
                left++;
                i--;
            }
            else {
                break;
            }
        }
        // There are several links down the shaft
        for (int i = chessClass.getY() + 1; i <= 19 && i <= chessClass.getY() + 4; i++) {
            if (arr[chessClass.getX()][i] && chessClass.getColor().equals(colors[chessClass.getX()][i])) {
                right++;
                count++;
            }else if (i<=17 && !arr[chessClass.getX()][i] && arr[chessClass.getX()][i+1] && lblank==0 && rblank == 0 && chessClass.getColor().equals(colors[chessClass.getX()][i+1])) {
                rblank++;
                count++;
                right++;
                i++;
            }
            else {
                break;
            }
        }
        // The vertical line is empty at both ends
        if (count == 3 && (chessClass.getY()-left-lblank-1>=0) && !arr[chessClass.getX()][chessClass.getY()-left-lblank-1] &&(chessClass.getY()+right+rblank+1<=19)&& !arr[chessClass.getX()][chessClass.getY()+right+rblank+1]) {
            live3++;
            System.out.println("shuxian");
            if(live3>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"双活三\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Four vertical lines with two empty ends
        if (count == 4 && (chessClass.getY()-left-lblank-1>=0) && !arr[chessClass.getX()][chessClass.getY()-left-lblank-1] &&(chessClass.getY()+right+rblank+1<=19)&& !arr[chessClass.getX()][chessClass.getY()+right+rblank+1]) {
            live4++;
            System.out.println("shuxian");
            if(live4>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"活四\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Whether the vertical line is five
        if (count > 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"长连\" —— Forbidden Move");
            alert.setTitle("Warning");
            alert.setHeaderText("Warning: ");
            ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(confirmButton);
            alert.show();
            return true;
        }
        count = 1;
        left = 0;
        right = 0;
        lblank = 0;
        rblank = 0;

        // There are several links on the left slope
        for (int i = chessClass.getX() - 1, j = chessClass.getY() - 1;
             i >= 0 &&  i >= chessClass.getX() - 4 && j >= 0 &&
                     j >= chessClass.getY() - 4; i--, j--) {
            if (arr[i][j] && chessClass.getColor().equals(colors[i][j])) {
                count++;
                left++;
            }else if (i>=2 && j>=2 && !arr[i][j] && arr[i-1][j-1] && lblank==0 && chessClass.getColor().equals(colors[i-1][j-1])) {
                lblank++;
                count++;
                left++;
                i--;
                j--;
            }
            else {
                break;
            }

        }

        // There are several links under the right oblique
        for (int i = chessClass.getX() + 1, j = chessClass.getY() + 1;
             i <= 19 &&  i <= chessClass.getX() + 4 && j <= 19 &&
                     j <= chessClass.getY() + 4; i++, j++) {
            if (arr[i][j] && chessClass.getColor().equals(colors[i][j])) {
                right++;
                count++;
            }else if (i<=17 && j<=17 && !arr[i][j] && arr[i+1][j+1] && lblank==0 && rblank == 0 && chessClass.getColor().equals(colors[i+1][j+1])) {
                rblank++;
                count++;
                right++;
                i++;
                j++;
            }
            else {
                break;
            }

        }
        // The left oblique line has three empty ends
        if (count == 3 && (chessClass.getX()-left-lblank-1>=0)&& (chessClass.getY()-left-lblank-1>=0)
                &&!arr[chessClass.getX()-left-lblank-1][chessClass.getY()-left-lblank-1]
                && (chessClass.getX()+right+rblank+1<=19)&&(chessClass.getY()+right+rblank+1<=19)
                && !arr[chessClass.getX()+right+rblank+1][chessClass.getY()+right+rblank+1]) {
            live3++;
            System.out.println("-x xian");
            if(live3>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"双活三\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Both ends of the left oblique line are empty
        if (count == 4 && (chessClass.getX()-left-lblank-1>=0)&& (chessClass.getY()-left-lblank-1>=0)
                &&!arr[chessClass.getX()-left-lblank-1][chessClass.getY()-left-lblank-1]
                && (chessClass.getX()+right+rblank+1<=19)&&(chessClass.getY()+right+rblank+1<=19)
                && !arr[chessClass.getX()+right+rblank+1][chessClass.getY()+right+rblank+1]) {
            live4++;
            System.out.println("-x xian");
            if(live4>=2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"活四\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Whether the right oblique line is five consecutive
        if (count > 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"长连\" —— Forbidden Move");
            alert.setTitle("Warning");
            alert.setHeaderText("Warning: ");
            ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(confirmButton);
            alert.show();
            return true;
        }
        count = 1;
        left = 0;
        right = 0;
        lblank = 0;
        rblank = 0;
        // There are several links on the right slope
        for (int i = chessClass.getX() + 1, j = chessClass.getY() - 1;
             i <= 19 &&  i <= chessClass.getX() + 4 && j >= 0 &&
                     j >= chessClass.getY() - 4; i++, j--) {
            if (arr[i][j] && chessClass.getColor().equals(colors[i][j])) {
                right++;
                count++;
            }else if (i<=17 && j>=2 && !arr[i][j] && arr[i+1][j-1]  && rblank == 0 && chessClass.getColor().equals(colors[i+1][j-1])) {
                rblank++;
                count++;
                right++;
                i++;
                j--;
            }
            else {
                break;
            }

        }

        // There are several links under the left oblique
        for (int i = chessClass.getX() - 1, j = chessClass.getY() + 1;
             i >= 0 &&  i >= chessClass.getX() - 4 && j <= 19 &&
                     j <= chessClass.getY() + 4; i--, j++) {
            if (arr[i][j] && chessClass.getColor().equals(colors[i][j])) {
                left++;
                count++;
            }else if (i>=2 && j<=17 && !arr[i][j] && arr[i-1][j+1] && lblank==0 && rblank == 0 && chessClass.getColor().equals(colors[i-1][j+1])) {
                lblank++;
                count++;
                left++;
                i--;
                j++;
            }
            else {
                break;
            }

        }
        // The right oblique line has three empty ends
        if (count == 3 && (chessClass.getX()+right+rblank+1<=19)&&(chessClass.getY()-right-rblank-1>=0)
                && !arr[chessClass.getX()+right+rblank+1][chessClass.getY()-right-rblank-1]
                && (chessClass.getX()-left-lblank-1>=0) && (chessClass.getY()+left+lblank+1<=19)
                && !arr[chessClass.getX()-left-lblank-1][chessClass.getY()+left+lblank+1]) {
            live3++;
            System.out.println("x xian");
            if(live3>=2){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"双活三\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Both ends of the right oblique line are empty
        if (count == 4 && (chessClass.getX()+right+rblank+1<=19)&&(chessClass.getY()-right-rblank-1>=0)
                && !arr[chessClass.getX()+right+rblank+1][chessClass.getY()-right-rblank-1]
                && (chessClass.getX()-left-lblank-1>=0) && (chessClass.getY()+left+lblank+1<=19)
                && !arr[chessClass.getX()-left-lblank-1][chessClass.getY()+left+lblank+1]) {
            live4++;
            System.out.println("x xian");
            if(live4>=2){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"活四\" —— Forbidden Move");
                alert.setTitle("Warning");
                alert.setHeaderText("Warning: ");
                ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(confirmButton);
                alert.show();
                return true;
            }
        }
        // Whether the right oblique line is five consecutive
        if (count > 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "\"长连\" —— Forbidden Move");
            alert.setTitle("Warning");
            alert.setHeaderText("Warning: ");
            ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(confirmButton);
            alert.show();
            return true;
        }
        return false;
    }
}
