import { Component } from '@angular/core';
import { NavComponent } from "../components/nav/nav";
import { FooterComponent } from "../components/footer/footer";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-history',
  imports: [NavComponent, FooterComponent, FormsModule],
  templateUrl: './history.html',
  styleUrl: './history.css'
})
export class HistoryPageComponent {

   history = [
    { date: '2025-10-05', news: 'Did Cardinal Luis Tagle speak about corruption?', status: 'Verified', verdict: 'True' },
    { date: '2025-05-10', news: 'Scientists announced immortality formula', status: 'Verified', verdict: 'False' },
    { date: '2025-10-05', news: 'Stock Market Rise caused by AI robots', status: 'Verified', verdict: 'False' },
    { date: '2025-09-22', news: 'Facebook to start charging monthly subscription for free users', status: 'Verified', verdict: 'False' },
    { date: '2025-07-18', news: 'Philippine peso to be replaced by digital currency in 2026', status: 'Verified', verdict: 'False' },
    { date: '2025-08-09', news: 'Department of Education to implement four-day school week nationwide', status: 'Verified', verdict: 'True' },
    { date: '2025-04-12', news: 'NASA confirms alien life discovered on Mars', status: 'Verified', verdict: 'False' },
    { date: '2025-10-03', news: 'Local farmers receive free AI-powered drones from DA', status: 'Verified', verdict: 'True' },
    { date: '2025-09-05', news: 'Government to ban all gasoline vehicles by 2030', status: 'Verified', verdict: 'False' },
    { date: '2025-06-11', news: 'WHO declares global end to COVID-19 pandemic', status: 'Verified', verdict: 'True' },
    { date: '2025-08-28', news: 'China launches world’s first weather-controlling satellite', status: 'Verified', verdict: 'False' },
    { date: '2025-07-30', news: 'New AI model passes medical licensing exam with perfect score', status: 'Verified', verdict: 'True' },
    { date: '2025-05-21', news: 'Netflix to remove all Marvel movies by 2026', status: 'Verified', verdict: 'False' },
    { date: '2025-10-14', news: 'Philippine government to issue digital national ID cards', status: 'Verified', verdict: 'True' },
    { date: '2025-04-07', news: 'Elon Musk announces human colony plans on Venus', status: 'Verified', verdict: 'False' },
    { date: '2025-09-17', news: 'Researchers find cure for Type 1 Diabetes using gene therapy', status: 'Verified', verdict: 'True' },
    { date: '2025-08-02', news: 'Massive sinkhole appears near Quezon City Circle', status: 'Verified', verdict: 'False' },
    { date: '2025-06-25', news: 'DOH confirms no recorded dengue cases in past two months', status: 'Verified', verdict: 'True' },
    { date: '2025-07-05', news: 'Philippines ranked top destination in Asia for digital nomads', status: 'Verified', verdict: 'True' },
    { date: '2025-09-01', news: 'Apple introduces iPhone with built-in hologram projector', status: 'Verified', verdict: 'False' },
    { date: '2025-05-14', news: 'Senate passes bill mandating AI ethics courses in universities', status: 'Verified', verdict: 'True' },
    { date: '2025-08-15', news: 'Man claims to time travel to 2080 and brings proof', status: 'Verified', verdict: 'False' },
    { date: '2025-09-12', news: 'Pag-IBIG Fund increases housing loan limit to ₱10 million', status: 'Verified', verdict: 'True' },
    { date: '2025-07-25', news: 'Google to discontinue Gmail service by 2026', status: 'Verified', verdict: 'False' },
    { date: '2025-06-19', news: 'Philippine Energy Department announces 100% renewable power by 2040', status: 'Verified', verdict: 'True' },
    { date: '2025-04-23', news: 'PAGASA predicts snowfall in Baguio this December', status: 'Verified', verdict: 'False' },
    { date: '2025-09-30', news: 'AI successfully translates ancient undeciphered scripts', status: 'Verified', verdict: 'True' },
    { date: '2025-07-02', news: 'McDonald’s to replace all employees with robots in 2026', status: 'Verified', verdict: 'False' },
    { date: '2025-08-23', news: 'Filipino student wins global AI innovation award', status: 'Verified', verdict: 'True' },
    { date: '2025-10-01', news: 'Ocean turns green overnight due to algae bloom', status: 'Verified', verdict: 'True' },
    { date: '2025-09-10', news: 'Japan offers free travel packages to boost tourism', status: 'Verified', verdict: 'True' },
    { date: '2025-06-30', news: 'YouTube bans all AI-generated music videos', status: 'Verified', verdict: 'False' },
    { date: '2025-07-20', news: 'Local startup develops 3D-printed sustainable housing', status: 'Verified', verdict: 'True' },
    { date: '2025-05-01', news: 'WHO confirms caffeine causes short-term memory loss', status: 'Verified', verdict: 'False' },
    { date: '2025-08-12', news: 'LRT and MRT to merge under one smart card system', status: 'Verified', verdict: 'True' },
    { date: '2025-04-30', news: 'NASA to launch Filipino astronaut on 2026 moon mission', status: 'Verified', verdict: 'True' },
    { date: '2025-09-03', news: 'TikTok to start paying users per video view globally', status: 'Verified', verdict: 'False' },
    { date: '2025-06-04', news: 'AI chatbot elected as honorary mayor in Japan town', status: 'Verified', verdict: 'True' },
    { date: '2025-05-26', news: 'Scientists prove that eating ice cream boosts intelligence', status: 'Verified', verdict: 'False' },
    { date: '2025-08-29', news: 'DOST launches AI-powered disaster prediction platform', status: 'Verified', verdict: 'True' },
    { date: '2025-10-02', news: 'BSP announces new ₱1000 polymer note design', status: 'Verified', verdict: 'True' },
    { date: '2025-07-09', news: 'You can now charge your phone using Wi-Fi', status: 'Verified', verdict: 'False' },
    { date: '2025-09-18', news: 'Filipino inventor creates water-powered motorcycle', status: 'Verified', verdict: 'True' },
    { date: '2025-06-10', news: 'UN declares internet access a basic human right', status: 'Verified', verdict: 'True' },
    { date: '2025-04-15', news: 'Google Maps to include time travel view feature', status: 'Verified', verdict: 'False' },
    { date: '2025-05-19', news: 'DepEd rolls out nationwide AI grading system', status: 'Verified', verdict: 'True' },
    { date: '2025-08-05', news: 'Man claims to survive two weeks eating only bubble tea', status: 'Verified', verdict: 'False' },
    { date: '2025-10-07', news: 'Scientists design bacteria that eats plastic waste', status: 'Verified', verdict: 'True' },
    { date: '2025-09-25', news: 'Metro Manila to adopt 24-hour curfew for AI testing', status: 'Verified', verdict: 'False' },
    { date: '2025-06-21', news: 'Filipino teacher wins global education innovation award', status: 'Verified', verdict: 'True' },
    { date: '2025-01-21', news: "Mapuan Student, Juoross Phillip Jose, met Manny Pacquiao", status: 'Verified', verdict: 'True'}
  ];

  searchQuery = '';

  filteredHistory() {
    const query = this.searchQuery.toLowerCase().trim();
    if (!query) return this.history;
    return this.history.filter(item =>
      item.news.toLowerCase().includes(query) ||
      item.date.toLowerCase().includes(query) ||
      item.verdict.toLowerCase().includes(query)
    );
  }



}
