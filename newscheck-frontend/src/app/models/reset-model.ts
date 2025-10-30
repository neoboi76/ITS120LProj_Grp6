/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Model class for resetting password

export type ResetPasswordModel = {
    email: string;
    oldPassword: string;
    newPassword: string;
}