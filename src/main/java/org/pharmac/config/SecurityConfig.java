package org.pharmac.config;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.config.CustomUserDetailsService;
import org.pharmac.models.PharmacySettings;
import org.pharmac.repository.GestionUtilisateurRepository;
import org.pharmac.repository.PharmacySettingsRepository;
import org.pharmac.repository.RoleRepository;
import org.pharmac.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@SpringBean
	private RoleService roleService;

	private CustomUserDetailsService userDetailsService;


//	@Bean
//	public UserDetailsService userService(UserRepository repository) {
//		return username -> repository.findByUsername(username)
//				.asUser();
//	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/signin", "/resource/**").permitAll() // autorise l'accès aux ressources statiques pour tout le monde
				.antMatchers("/accueil", "/categories", "/fournisseurs","/details-produit", "/produits", "/new-password", "/settings", "/profil").authenticated() // autorise l'accès aux utilisateurs authentifiés
				.antMatchers("/vendeur/**").hasAuthority("VENDEUR") // autorise l'accès aux vendeurs et aux administrateurs
				.antMatchers("/gestionnaire/**").hasAuthority("GESTIONNAIRE_STOCK") // autorise l'accès aux gestionnaires de stock et aux administrateurs
				.antMatchers("/admin/**").hasAuthority("ADMIN") // autorise l'accès uniquement aux administrateurs
//				.anyRequest().denyAll() // refuse l'accès à toutes les autres requêtes
				.and()
				.formLogin().loginPage("/signin").defaultSuccessUrl("/accueil", true).permitAll().and()
				.csrf().disable()
				.headers().frameOptions().disable()
				.and()
				.httpBasic().disable();

	}


//	@Bean
//	SecurityFilterChain defaultSecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.authorizeRequests()
//				.antMatchers("/edit-categorie", "/edit-fournisseur", "/edit-stock", "/new-vente", "/invoice", "/info-produit").hasRole("ADMIN")
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.and()
//				.httpBasic();
//		return httpSecurity.build();
//	}

//	@Bean
//	CommandLineRunner initRoles(RoleRepository repository) {
//		return args -> {
//
//		};
//	}

	@Bean
	CommandLineRunner initRolesAndUsers(GestionUtilisateurRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder, PharmacySettingsRepository pharmaRepo) {
		return args -> {
//			Role roleAdmin = new Role("ADMIN", "Administrateur");
//			roleRepo.save(roleAdmin);
//			Role roleGesStk = new Role("GESTIONNAIRE_STOCK","Gestionnaire des stocks");
//			roleRepo.save(roleGesStk);
//			Role roleVen = new Role("VENDEUR","Vendeur");
//			roleRepo.save(roleVen);
//			userRepo.save(new Utilisateur(
//					"KEADMINPHARMA",
//					"keadmin",
//					"Eddie",
//					"Kapou",
//					passwordEncoder.encode("password"),
//					"92800155",
//					"eddiekapou@gmail.com",
//					"Attiegou, Lomé",
//					true,
//					roleAdmin, roleGesStk, roleVen));
//			userRepo.save(new Utilisateur(
//					"PHARMADOR",
//					"uservendor",
//					"Jean",
//					"DAW",
//					passwordEncoder.encode("motdepasse"),
//					"90000000",
//					"jeandaw2024@gmail.com",
//					"Wuiti, Lomé",
//					true,
//					roleVen));
//			userRepo.save(new Utilisateur(
//					"PHARMASTOCK",
//					"managstk",
//					"Kossi",
//					"EFI",
//					passwordEncoder.encode("motdepasse"),
//					"90005050",
//					"esstickkof@gmail.com",
//					"Wuiti, Lomé",
//					true,
//					roleGesStk));
//			pharmaRepo.save(new PharmacySettings(
//					"ABC",
//					"Wuiti",
//					"90000000",
//					"xyz@gmail.com",
//					"Nouvelle pharmacie",
//					"Clair"
//			));
		};
	}
}
