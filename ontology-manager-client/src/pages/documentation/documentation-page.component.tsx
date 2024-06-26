
import { Accordion, AccordionTab } from 'primereact/accordion';
import { PageTitle } from '../../components';
import { toolDisclaimer } from '../../utils';
        
const React = require('react');

const DocumentationPageComponent: React.FC = () => {
    return <div className='page-container-narrow'>
    
    <PageTitle icon='pi pi-book' title='Documentation' help={false}></PageTitle>
    
    <Accordion multiple activeIndex={[]}>
             
    <AccordionTab header="Mission">
        <p className="m-0">
        The creation of an advanced data collection and analysis software tool dedicated to empower the joined research and knowledge exchange of the <b>GLACIER</b> consortium (and beyond) on plant-derived natural products and their usage for the treatment of infectious diseases, with focus on the flora and epidemiological needs of Latin-America. <br/><br/>
        <b>GLACIER</b> (German Latin-American Center for Infection and Epidemiology Research &amp; Training) is an international multidisciplinary consortium that aims to level regional disparities in Latin America by strengthening surveillance and response to emerging infectious diseases and developing new vaccines and therapies. GLACIER is made up of more than 30 cooperating institutions from nine countries (Germany, Mexico, Cuba, Costa Rica, Nicaragua, Honduras, El Salvador, Panama, and Guatemala).
For further information, please visit the GLACIER consortium’s official website <a href='https://glacieronehealth.org/about/' target='blank'>About | GLACIER One Health Project</a>
        </p>
    </AccordionTab>
    <AccordionTab header="Location">
        <p className="m-0">
            This data collection and analysis software tool is a project developed and curated by the Leibniz Institute of Plant Biochemistry (IPB, Weinberg 3, D-06120 Halle (Saale), Germany) in scope of the GLACIER consortium.
        </p>
    </AccordionTab>
    <AccordionTab header="Funding">
        <p className="m-0">
            The development of this tool, as part of the activities of the <b>GLACIER</b> consortium, is funded by the <b>DAAD</b>, Germany (Deutscher Akademischer Austauschdienst/German Academic Exchange Service; funding code DAAD 57592717).<br/><br/>
            <b>GLACIER</b> (German Latin-American Center for Infection and Epidemiology Research &amp; Training) is an international multidisciplinary consortium that aims to level regional disparities in Latin America by strengthening surveillance and response to emerging infectious diseases and developing new vaccines and therapies. GLACIER is made up of more than 30 cooperating institutions from nine countries (Germany, Mexico, Cuba, Costa Rica, Nicaragua, Honduras, El Salvador, Panama, and Guatemala).
For further information, please visit the GLACIER consortium’s official website <a href='https://glacieronehealth.org/about/' target='blank'>About | GLACIER One Health Project</a>
        </p>
    </AccordionTab>
    <AccordionTab header="References">

        <b>Collection of Open Natural Products (COCONUT)</b>
        <p >
            Sorokina, M., Merseburger, P., Rajan, K. et al. COCONUT online: Collection of Open Natural Products database. J Cheminform 13, 2 (2021). <a href='https://doi.org/10.1186/s13321-020-00478-9' target='blank'>https://doi.org/10.1186/s13321-020-00478-9</a>
        </p>
        
        <b>ChEMBL Database</b>
        <p >
        Zdrazil B, Felix E, Hunter F, Manners EJ, Blackshaw J, Corbett S, de Veij M, Ioannidis H, Lopez DM, Mosquera JF, Magarinos MP, Bosc N, Arcila R, Kizilören T, Gaulton A, Bento AP, Adasme MF, Monecke P, Landrum GA, Leach AR. The ChEMBL Database in 2023: a drug discovery platform spanning multiple bioactivity data types and time periods. Nucleic Acids Res. 2024 Jan 5;52(D1):D1180-D1192. 
        <a href='http://dx.doi.org/10.1093/nar/gkv352' target='blank'>10.1093/nar/gkv352 </a>
        </p>
        
        <b>ChEBI</b>
        <p>
        Hastings J, Owen G, Dekker A, Ennis M, Kale N, Muthukrishnan V, Turner S, Swainston N, Mendes P, Steinbeck C. (2016). ChEBI in 2016: Improved services and an expanding collection of metabolites. <a href='http://europepmc.org/abstract/MED/26467479' target='blank'>Nucleic Acids Res.</a>
        </p>
        
        <b>MPDB 2.0</b>
        <p>
        Nazmul Hussain, Rony Chanda, Ruhshan Ahmed Abir, Mohsina Akter Mou, Md. Kamrul Hasan & M. Arif Ashraf. (2021). MPDB 2.0: a large scale and integrated medicinal plant database of Bangladesh. <a href='https://bmcresnotes.biomedcentral.com/articles/10.1186/s13104-021-05721-6' target='blank'>BMC Research Notes</a>, 14, 301 (2021).
        </p>

        <b>Dispel</b>
        <p>
        Kavya Singh, Harshit Maurya, Parthasarathi Singh, Pujarani Panda, Amit Kumar Behera, Arshad Jamal, Ganesh Eslavath, Somesh Mohapatra, Harsh Chauhan, Deepak Sharma, DISPEL: database for ascertaining the best medicinal plants to cure human diseases, Database, Volume 2023, 2023, baad073, <a href='https://doi.org/10.1093/database/baad073' target='blank'>https://doi.org/10.1093/database/baad073</a></p>

        <b>KNApSAcK</b>
        <p>
        Farit Mochamad Afendi, Taketo Okada, Mami Yamazaki, Aki Hirai-Morita, Yukiko Nakamura, Kensuke Nakamura, Shun Ikeda, Hiroki Takahashi, Md. Altaf-Ul-Amin, Latifah K. Darusman, Kazuki Saito, Shigehiko Kanaya, KNApSAcK Family Databases: Integrated Metabolite–Plant Species Databases for Multifaceted Plant Research, Plant and Cell Physiology, Volume 53, Issue 2, February 2012, Page e1, <a href='https://doi.org/10.1093/pcp/pcr165' target='blank'>https://doi.org/10.1093/pcp/pcr165</a>
        </p>

        <b>NPASS</b>
        <p>
        Xian Zeng, Peng Zhang, Weidong He, Chu Qin, Shangying Chen, Lin Tao, Yali Wang, Ying Tan, Dan Gao, Bohua Wang, Zhe Chen, Weiping Chen, Yu Yang Jiang, Yu Zong Chen, NPASS: natural product activity and species source database for natural product research, discovery and tool development, Nucleic Acids Research, Volume 46, Issue D1, 4 January 2018, Pages D1217–D1222, <a href='https://doi.org/10.1093/nar/gkx1026' target='blank'>https://doi.org/10.1093/nar/gkx1026</a>
        </p>

        <b>The Disease Ontology (DO)</b>
        <p>
        Lynn Marie Schriml, Cesar Arze, Suvarna Nadendla, Yu-Wei Wayne Chang, Mark Mazaitis, Victor Felix, Gang Feng, Warren Alden Kibbe, Disease Ontology: a backbone for disease semantic integration, Nucleic Acids Research, Volume 40, Issue D1, 1 January 2012, Pages D940–D946, <a href='https://doi.org/10.1093/nar/gkr972' target='blank'>https://doi.org/10.1093/nar/gkr972</a>
        </p>

        <b>Drug Central</b>
        <p>
        Ursu O, Holmes J, Knockel J, Bologa CG, Yang JJ, Mathias SL, Nelson SJ, Oprea TI. DrugCentral: online drug compendium. Nucleic Acids Res. 2017 Jan 4;45(D1):D932-D939. doi: <a href='https://doi.org/10.1093%2Fnar%2Fgkw993' target='blank'>10.1093/nar/gkw993</a>. Epub 2016 Oct 26. PMID: 27789690; PMCID: PMC5210665.
        </p>

        <b>DrugBank</b>
        <p>
        David S. Wishart, Craig Knox, An Chi Guo, Dean Cheng, Savita Shrivastava, Dan Tzur, Bijaya Gautam, Murtaza Hassanali, DrugBank: a knowledgebase for drugs, drug actions and drug targets, Nucleic Acids Research, Volume 36, Issue suppl_1, 1 January 2008, Pages D901–D906, <a href='https://doi.org/10.1093/nar/gkm958' target='blank'>https://doi.org/10.1093/nar/gkm958</a>
        </p>

        <b>Vascular plants for Cuba. Inventory. Third Edition</b>
        <p>
        Greuter W. & Rankin Rodríguez R. 2022: Vascular Plants of Cuba. Inventory. Third edition, updated, of Espermatophytes from Cuba. Vascular Plants of Cuba. A checklist. Third, updated edition of The Spermatophyta of Cuba. – Berlin: Botanical Garden and Botanical Museum Berlin; Havana: National Botanical Garden, University of Havana. – ISBN 978-3-946292-42-5, <a href='https://doi.org/10.3372/cubalist.2022.1'>https://doi.org/10.3372/cubalist.2022.1</a>
        </p>



    </AccordionTab>
    <AccordionTab header="Disclaimer">
    At the moment, this tool is still primarily focused on the analysis of data of Cuban plants. Hence, the output for plants from other geographical regions might be weaker.
This tool shows connections of available data which could be <b>erroneous</b>, <b>biased</b>  and <b>not necessarily the most relevant</b> one for your scientific question since  it  was <b>extracted from available data sources with an automatic process</b>. 
We are actively working to expand the data basis (please contribute!) and improve the data quality of the platform.

    </AccordionTab>
    </Accordion>
    
    </div>;
};

export default DocumentationPageComponent;
