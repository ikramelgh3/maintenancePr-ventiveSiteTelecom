import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {User} from "../../models/user";
import { switchMap } from 'rxjs/operators';
import { EMPTY } from 'rxjs';
import {CentreTechnique} from "../../models/centreTechnique";
@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent implements OnInit {
  addForm!:FormGroup
roles:string[]=["RESPONSABLE DE MAINTENANCE", "TECHNICIEN", "INJECTEUR", "REPORTING"]
  type:string[] =["Interne","Externe"]


  cites:string[]  = [
    "Agadir",
    "Al Hoceima",
    "Beni Mellal",
    "Casablanca",
    "Dakhla",
    "El Jadida",
    "Essaouira",
    "Fès",
    "Kenitra",
    "Laâyoune",
    "Marrakech",
    "Meknès",
    "Mohammedia",
    "Nador",
    "Oujda",
    "Rabat",
    "Salé",
    "Tanger",
    "Taza",
    "Tetouan"
  ];
CentreTechniques!:CentreTechnique[]
  ngOnInit() {
    this.getCentreTec()
  }
  centr: string[] = [];
  constructor(
    private ser: PlanningServiceService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<NewUserComponent>,) {
    dialogRef.disableClose = true;
    this.addForm= this.fb.group({
      firstName: ['', [Validators.minLength(2), Validators.required]],
      lastName: ['', [Validators.minLength(2), Validators.required]],
      city: ['', [
        Validators.required,

      ]],

      available:['', [
        Validators.required,

      ]],
      email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
      phone_number: ['', [Validators.required,Validators.minLength(10), Validators.pattern(/^[\d+\-]+$/)]],
      roleName: this.fb.control('', Validators.required),
      userName: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[!@#$%^&*])/)
      ]],
      confirmPassword: ['', Validators.required],
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(14),
        this.passwordComplexityValidator(),
        this.passwordDifferentFromUsernameValidator()
      ]),
      type: this.fb.control(''),
    });

    const typeControl = this.addForm.get('type');
    this.addForm.get('roleName')?.valueChanges.subscribe(role => {
      if (role === 'TECHNICIEN') {
        typeControl?.setValidators(Validators.required);
      } else {
        typeControl?.clearValidators();
      }
      typeControl?.updateValueAndValidity();
    });
  }
  sendingEmailSnackBar!: MatSnackBarRef<any>;

user!:User

  getCentreTec() {
    this.ser.getAllCT().subscribe((data: CentreTechnique[]) => {
      this.CentreTechniques = data;
      this.centr = data.map(centre => centre.name);  // Extraire les noms des centres techniques
    });
  }
  addUser() {

    if (this.addForm.valid) {

      console.log(this.addForm.get('IdCentreTechnique')?.value)
      const password = this.addForm.get('password')?.value;
      const confirmPassword = this.addForm.get('confirmPassword')?.value;
      if (password !== confirmPassword) {
        this.snackBar.open('Les mots de passe ne correspondent pas', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        return;
      }

      const passwordValidity = this.checkPasswordValidity(password);
      if (!passwordValidity.valid) {
        this.snackBar.open(passwordValidity.message, 'Fermer', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        return;
      }

      const username = this.addForm.get('userName')?.value;
      const email = this.addForm.get('email')?.value;

      // Vérification de l'existence du nom d'utilisateur et de l'e-mail
      this.ser.checkUserExistByUserName(username).pipe(
        switchMap((usernameExists: boolean) => {
          if (usernameExists) {
            this.snackBar.open('Le nom d\'utilisateur existe déjà', 'Fermer', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
            return EMPTY; // Arrête l'exécution du flux
          } else {
            return this.ser.checkUserExistByEmail(email); // Passe à la vérification de l'e-mail
          }
        }),
        switchMap((emailExists: boolean) => {
          if (emailExists) {
            this.snackBar.open('L\'adresse e-mail existe déjà', 'Fermer', {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });
            return EMPTY; // Arrête l'exécution du flux
          } else {
            // Afficher la notification "Envoi de l'e-mail en cours..."
            const sendingEmailSnackBar = this.snackBar.open('L\e-mail contenant les informations de connexion de votre utilisateur est en cours d\'envoi. Veuillez patienter', '', {
              duration: -1, // Durée indéfinie jusqu'à ce que l'e-mail soit envoyé
              horizontalPosition: 'end',
              verticalPosition: 'top'
            });

            return this.ser.addUser(this.addForm.value); // Ajoute l'utilisateur s'il n'existe pas déjà
          }
        })
      ).subscribe(() => {
        this.snackBar.open('Utilisateur ajouté avec succès', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.close();
      }, () => {
        this.snackBar.open('Erreur lors de l\'ajout de l\'utilisateur', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      });

    } else {
      this.snackBar.open('Veuillez remplir correctement tous les champs du formulaire', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
    }
  }

  checkPasswordValidity(password: string): { valid: boolean, message: string } {
    if (password.length < 8) {
      return { valid: false, message: 'Le mot de passe doit avoir au moins 8 caractères' };
    } else if (password.length > 14) {
      return { valid: false, message: 'Le mot de passe ne doit pas dépasser 14 caractères' };
    } else if (!/[a-zA-Z0-9]/.test(password)) {
      return { valid: false, message: 'Le mot de passe doit être alphanumérique' };
    } else if (!/(?=.*[!@#$%^&*])/.test(password)) {
      return { valid: false, message: 'Le mot de passe doit inclure un caractère spécial' };
    } else {
      return { valid: true, message: '' };
    }
  }

  passwordHasError = false;
  passwordErrorMessage = '';
  checkPassword() {
    const password = this.addForm.get('password')?.value;
    const username = this.addForm.get('userName')?.value;
    if (password === username) {
      this.passwordHasError = true;
      this.passwordErrorMessage = 'Le mot de passe ne doit pas être identique au nom d\'utilisateur';
    } else if (password.length < 8) {
      this.passwordHasError = true;
      this.passwordErrorMessage = 'Le mot de passe doit avoir au moins 8 caractères';
    } else if (password.length > 14) {
      this.passwordHasError = true;
      this.passwordErrorMessage = 'Le mot de passe ne doit pas dépasser 14 caractères';
    } else if (!/[a-zA-Z0-9]/.test(password)) {
      this.passwordHasError = true;
      this.passwordErrorMessage = 'Le mot de passe doit être alphanumérique';
    } else if (!/(?=.*[!@#$%^&*])/.test(password)) {
      this.passwordHasError = true;
      this.passwordErrorMessage = 'Le mot de passe doit inclure un caractère spécial';
    } else {
      this.passwordHasError = false;
      this.passwordErrorMessage = '';
    }

    if (this.passwordHasError) {
      this.snackBar.open(this.passwordErrorMessage, 'Fermer', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
    }
  }


  passwordComplexityValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const password = control.value;
      if (!password) return null;

      const hasUpperCase = /[A-Z]/.test(password);
      const hasLowerCase = /[a-z]/.test(password);
      const hasNumber = /[0-9]/.test(password);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

      const valid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
      return !valid ? { passwordComplexity: true } : null;
    };
  }
  passwordMismatch = false;
  checkPasswordsMatch() {
    const password = this.addForm.get('password')?.value;
    const confirmPassword = this.addForm.get('confirmPassword')?.value;
    if (password !== confirmPassword) {
      this.passwordMismatch = true;
      this.snackBar.open('Les mots de passe ne correspondent pas', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
    } else {
      this.passwordMismatch = false;
    }
  }
  passwordDifferentFromUsernameValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const password = control.value;
      const username = this.addForm?.get('userName')?.value;
      if (!password || !username) return null;

      return password === username ? { passwordSameAsUsername: true } : null;
    };
  }
  getErrorMessageForPhoneNumber() {
    return this.addForm.get('phone_number')?.hasError('required') ? 'Veuillez remplir le champs' :
      this.addForm.get('phone_number')?.hasError('minlength') ? 'Le numéro de téléphone doit contenir au moins 10 chiffres' :
      this.addForm.get('phone_number')?.hasError('pattern') ? 'Le numéro de téléphone n\'est pas valide' :
        '';
  }

  getErrorMessageForUserName() {
    const userNameControl = this.addForm.get('userName');

    if (userNameControl?.hasError('required')) {
      return 'Veuillez remplir le champ';
    }

    if (userNameControl?.hasError('minlength')) {
      return 'Le nom d\'utilisateur doit contenir au moins 8 caractères';
    }
    if (userNameControl?.hasError('pattern')) {
      return 'Le nom d\'utilisateur doit contenir au moins un caractère spécial parmi !@#$%^&*';
    }



    return '';
  }

  validateDomain(control: FormControl) {
    const email = control.value;
    if (email && email.indexOf('@') !== -1) {
      const domain = email.split('@')[1];
      if (domain !== 'votre_domaine.com') { // Remplacez 'votre_domaine.com' par le domaine que vous souhaitez valider
        return { invalidDomain: true };
      }
    }
    return null;
  }
  close(){
    this.dialogRef.close();
  }
}
